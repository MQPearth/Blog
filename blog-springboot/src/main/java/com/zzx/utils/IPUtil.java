package com.zzx.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.zzx.exception.DbMakerConfigException;

/**
 * @powerby: https://github.com/lionsoul2014/ip2region
 */
public class IPUtil {

    public static String getCityInfo(String ip) {
        String dbPath = IPUtil.class.getResource("/ip2region.db").getPath();

        File file = new File(dbPath);
        if (!file.exists()) {
            System.out.println("Error: Invalid ip2region.db file");
        }
        try {
            DbSearcher searcher = new DbSearcher(new DbConfig(), dbPath);
            if (!Util.isIpAddress(ip)) {
                System.out.println("Error: Invalid ip address");
            }
            return searcher.btreeSearch(ip).getRegion();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static class DataBlock {
        private int cityId;
        private String region;
        private int dataPtr;

        public DataBlock(int var1, String var2, int var3) {
            this.cityId = var1;
            this.region = var2;
            this.dataPtr = var3;
        }

        public DataBlock(int var1, String var2) {
            this(var1, var2, 0);
        }

        public int getCityId() {
            return this.cityId;
        }

        public DataBlock setCityId(int var1) {
            this.cityId = var1;
            return this;
        }

        public String getRegion() {
            return this.region;
        }

        public DataBlock setRegion(String var1) {
            this.region = var1;
            return this;
        }

        public int getDataPtr() {
            return this.dataPtr;
        }

        public DataBlock setDataPtr(int var1) {
            this.dataPtr = var1;
            return this;
        }

        @Override
        public String toString() {
            return String.valueOf(this.cityId) + '|' + this.region + '|' + this.dataPtr;
        }
    }


    public static class DbConfig {
        private int totalHeaderSize;
        private int indexBlockSize;

        public DbConfig(int var1) throws DbMakerConfigException {
            if (var1 % 8 != 0) {
                throw new DbMakerConfigException("totalHeaderSize must be times of 8");
            } else {
                this.totalHeaderSize = var1;
                this.indexBlockSize = 8192;
            }
        }

        public DbConfig() throws DbMakerConfigException {
            this(16384);
        }

        public int getTotalHeaderSize() {
            return this.totalHeaderSize;
        }

        public DbConfig setTotalHeaderSize(int var1) {
            this.totalHeaderSize = var1;
            return this;
        }

        public int getIndexBlockSize() {
            return this.indexBlockSize;
        }

        public DbConfig setIndexBlockSize(int var1) {
            this.indexBlockSize = var1;
            return this;
        }
    }


    public static class DbSearcher {
        public static final int BTREE_ALGORITHM = 1;
        public static final int BINARY_ALGORITHM = 2;
        public static final int MEMORY_ALGORITYM = 3;
        private DbConfig dbConfig = null;
        private RandomAccessFile raf = null;
        private long[] HeaderSip = null;
        private int[] HeaderPtr = null;
        private int headerLength;
        private long firstIndexPtr = 0L;
        private long lastIndexPtr = 0L;
        private int totalIndexBlocks = 0;
        private byte[] dbBinStr = null;

        public DbSearcher(DbConfig var1, String var2) throws FileNotFoundException {
            this.dbConfig = var1;
            this.raf = new RandomAccessFile(var2, "r");
        }

        public DbSearcher(DbConfig var1, byte[] var2) {
            this.dbConfig = var1;
            this.dbBinStr = var2;
            this.firstIndexPtr = Util.getIntLong(var2, 0);
            this.lastIndexPtr = Util.getIntLong(var2, 4);
            this.totalIndexBlocks = (int)((this.lastIndexPtr - this.firstIndexPtr) / (long)IndexBlock.getIndexBlockLength()) + 1;
        }

        public DataBlock memorySearch(long var1) throws IOException {
            int var3 = IndexBlock.getIndexBlockLength();
            if (this.dbBinStr == null) {
                this.dbBinStr = new byte[(int)this.raf.length()];
                this.raf.seek(0L);
                this.raf.readFully(this.dbBinStr, 0, this.dbBinStr.length);
                this.firstIndexPtr = Util.getIntLong(this.dbBinStr, 0);
                this.lastIndexPtr = Util.getIntLong(this.dbBinStr, 4);
                this.totalIndexBlocks = (int)((this.lastIndexPtr - this.firstIndexPtr) / (long)var3) + 1;
            }

            int var4 = 0;
            int var5 = this.totalIndexBlocks;
            long var10 = 0L;

            int var12;
            int var13;
            while (var4 <= var5) {
                var12 = var4 + var5 >> 1;
                var13 = (int)(this.firstIndexPtr + (long)(var12 * var3));
                long var6 = Util.getIntLong(this.dbBinStr, var13);
                if (var1 < var6) {
                    var5 = var12 - 1;
                } else {
                    long var8 = Util.getIntLong(this.dbBinStr, var13 + 4);
                    if (var1 <= var8) {
                        var10 = Util.getIntLong(this.dbBinStr, var13 + 8);
                        break;
                    }

                    var4 = var12 + 1;
                }
            }

            if (var10 == 0L) {
                return null;
            } else {
                var12 = (int)(var10 >> 24 & 255L);
                var13 = (int)(var10 & 16777215L);
                int var14 = (int)Util.getIntLong(this.dbBinStr, var13);
                String var15 = new String(this.dbBinStr, var13 + 4, var12 - 4, "UTF-8");
                return new DataBlock(var14, var15, var13);
            }
        }

        public DataBlock memorySearch(String var1) throws IOException {
            return this.memorySearch(Util.ip2long(var1));
        }

        public DataBlock getByIndexPtr(long var1) throws IOException {
            this.raf.seek(var1);
            byte[] var3 = new byte[12];
            this.raf.readFully(var3, 0, var3.length);
            long var4 = Util.getIntLong(var3, 8);
            int var6 = (int)(var4 >> 24 & 255L);
            int var7 = (int)(var4 & 16777215L);
            this.raf.seek((long)var7);
            byte[] var8 = new byte[var6];
            this.raf.readFully(var8, 0, var8.length);
            int var9 = (int)Util.getIntLong(var8, 0);
            String var10 = new String(var8, 4, var8.length - 4, "UTF-8");
            return new DataBlock(var9, var10, var7);
        }

        public DataBlock btreeSearch(long var1) throws IOException {
            int var4;
            int var5;
            if (this.HeaderSip == null) {
                this.raf.seek(8L);
                byte[] var3 = new byte[this.dbConfig.getTotalHeaderSize()];
                this.raf.readFully(var3, 0, var3.length);
                var4 = var3.length >> 3;
                var5 = 0;
                this.HeaderSip = new long[var4];
                this.HeaderPtr = new int[var4];

                for (int var10 = 0; var10 < var3.length; var10 += 8) {
                    long var6 = Util.getIntLong(var3, var10);
                    long var8 = Util.getIntLong(var3, var10 + 4);
                    if (var8 == 0L) {
                        break;
                    }

                    this.HeaderSip[var5] = var6;
                    this.HeaderPtr[var5] = (int)var8;
                    ++var5;
                }

                this.headerLength = var5;
            }

            if (var1 == this.HeaderSip[0]) {
                return this.getByIndexPtr((long)this.HeaderPtr[0]);
            } else if (var1 == this.HeaderSip[this.headerLength - 1]) {
                return this.getByIndexPtr((long)this.HeaderPtr[this.headerLength - 1]);
            } else {
                int var21 = 0;
                var4 = this.headerLength;
                var5 = 0;
                int var22 = 0;

                int var7;
                while (var21 <= var4) {
                    var7 = var21 + var4 >> 1;
                    if (var1 == this.HeaderSip[var7]) {
                        if (var7 > 0) {
                            var5 = this.HeaderPtr[var7 - 1];
                            var22 = this.HeaderPtr[var7];
                        } else {
                            var5 = this.HeaderPtr[var7];
                            var22 = this.HeaderPtr[var7 + 1];
                        }
                        break;
                    }

                    if (var1 < this.HeaderSip[var7]) {
                        if (var7 == 0) {
                            var5 = this.HeaderPtr[var7];
                            var22 = this.HeaderPtr[var7 + 1];
                            break;
                        }

                        if (var1 > this.HeaderSip[var7 - 1]) {
                            var5 = this.HeaderPtr[var7 - 1];
                            var22 = this.HeaderPtr[var7];
                            break;
                        }

                        var4 = var7 - 1;
                    } else {
                        if (var7 == this.headerLength - 1) {
                            var5 = this.HeaderPtr[var7 - 1];
                            var22 = this.HeaderPtr[var7];
                            break;
                        }

                        if (var1 <= this.HeaderSip[var7 + 1]) {
                            var5 = this.HeaderPtr[var7];
                            var22 = this.HeaderPtr[var7 + 1];
                            break;
                        }

                        var21 = var7 + 1;
                    }
                }

                if (var5 == 0) {
                    return null;
                } else {
                    var7 = var22 - var5;
                    int var23 = IndexBlock.getIndexBlockLength();
                    byte[] var9 = new byte[var7 + var23];
                    this.raf.seek((long)var5);
                    this.raf.readFully(var9, 0, var9.length);
                    var21 = 0;
                    var4 = var7 / var23;
                    long var14 = 0L;

                    int var16;
                    int var17;
                    while (var21 <= var4) {
                        var16 = var21 + var4 >> 1;
                        var17 = var16 * var23;
                        long var24 = Util.getIntLong(var9, var17);
                        if (var1 < var24) {
                            var4 = var16 - 1;
                        } else {
                            long var12 = Util.getIntLong(var9, var17 + 4);
                            if (var1 <= var12) {
                                var14 = Util.getIntLong(var9, var17 + 8);
                                break;
                            }

                            var21 = var16 + 1;
                        }
                    }

                    if (var14 == 0L) {
                        return null;
                    } else {
                        var16 = (int)(var14 >> 24 & 255L);
                        var17 = (int)(var14 & 16777215L);
                        this.raf.seek((long)var17);
                        byte[] var18 = new byte[var16];
                        this.raf.readFully(var18, 0, var18.length);
                        int var19 = (int)Util.getIntLong(var18, 0);
                        String var20 = new String(var18, 4, var18.length - 4, "UTF-8");
                        return new DataBlock(var19, var20, var17);
                    }
                }
            }
        }

        public DataBlock btreeSearch(String var1) throws IOException {
            return this.btreeSearch(Util.ip2long(var1));
        }

        public DataBlock binarySearch(long var1) throws IOException {
            int var3 = IndexBlock.getIndexBlockLength();
            if (this.totalIndexBlocks == 0) {
                this.raf.seek(0L);
                byte[] var4 = new byte[8];
                this.raf.readFully(var4, 0, var4.length);
                this.firstIndexPtr = Util.getIntLong(var4, 0);
                this.lastIndexPtr = Util.getIntLong(var4, 4);
                this.totalIndexBlocks = (int)((this.lastIndexPtr - this.firstIndexPtr) / (long)var3) + 1;
            }

            int var18 = 0;
            int var5 = this.totalIndexBlocks;
            byte[] var6 = new byte[var3];
            long var11 = 0L;

            int var13;
            while (var18 <= var5) {
                var13 = var18 + var5 >> 1;
                this.raf.seek(this.firstIndexPtr + (long)(var13 * var3));
                this.raf.readFully(var6, 0, var6.length);
                long var7 = Util.getIntLong(var6, 0);
                if (var1 < var7) {
                    var5 = var13 - 1;
                } else {
                    long var9 = Util.getIntLong(var6, 4);
                    if (var1 <= var9) {
                        var11 = Util.getIntLong(var6, 8);
                        break;
                    }

                    var18 = var13 + 1;
                }
            }

            if (var11 == 0L) {
                return null;
            } else {
                var13 = (int)(var11 >> 24 & 255L);
                int var14 = (int)(var11 & 16777215L);
                this.raf.seek((long)var14);
                byte[] var15 = new byte[var13];
                this.raf.readFully(var15, 0, var15.length);
                int var16 = (int)Util.getIntLong(var15, 0);
                String var17 = new String(var15, 4, var15.length - 4, "UTF-8");
                return new DataBlock(var16, var17, var14);
            }
        }

        public DataBlock binarySearch(String var1) throws IOException {
            return this.binarySearch(Util.ip2long(var1));
        }

        public DbConfig getDbConfig() {
            return this.dbConfig;
        }

        public void close() throws IOException {
            this.HeaderSip = null;
            this.HeaderPtr = null;
            this.dbBinStr = null;
            this.raf.close();
        }
    }


    public static class IndexBlock {
        private static int LENGTH = 12;
        private long startIp;
        private long endIp;
        private int dataPtr;
        private int dataLen;

        public IndexBlock(long var1, long var3, int var5, int var6) {
            this.startIp = var1;
            this.endIp = var3;
            this.dataPtr = var5;
            this.dataLen = var6;
        }

        public long getStartIp() {
            return this.startIp;
        }

        public IndexBlock setStartIp(long var1) {
            this.startIp = var1;
            return this;
        }

        public long getEndIp() {
            return this.endIp;
        }

        public IndexBlock setEndIp(long var1) {
            this.endIp = var1;
            return this;
        }

        public int getDataPtr() {
            return this.dataPtr;
        }

        public IndexBlock setDataPtr(int var1) {
            this.dataPtr = var1;
            return this;
        }

        public int getDataLen() {
            return this.dataLen;
        }

        public IndexBlock setDataLen(int var1) {
            this.dataLen = var1;
            return this;
        }

        public static int getIndexBlockLength() {
            return LENGTH;
        }

        public byte[] getBytes() {
            byte[] var1 = new byte[12];
            Util.writeIntLong(var1, 0, this.startIp);
            Util.writeIntLong(var1, 4, this.endIp);
            long var2 = (long)this.dataPtr | (long)(this.dataLen << 24) & 4278190080L;
            Util.writeIntLong(var1, 8, var2);
            return var1;
        }
    }


    public static class Util {
        public Util() {
        }

        public static void write(byte[] var0, int var1, long var2, int var4) {
            for (int var5 = 0; var5 < var4; ++var5) {
                var0[var1++] = (byte)((int)(var2 >>> 8 * var5 & 255L));
            }

        }

        public static void writeIntLong(byte[] var0, int var1, long var2) {
            var0[var1++] = (byte)((int)(var2 >> 0 & 255L));
            var0[var1++] = (byte)((int)(var2 >> 8 & 255L));
            var0[var1++] = (byte)((int)(var2 >> 16 & 255L));
            var0[var1] = (byte)((int)(var2 >> 24 & 255L));
        }

        public static long getIntLong(byte[] var0, int var1) {
            return (long)var0[var1++] & 255L | (long)(var0[var1++] << 8) & 65280L | (long)(var0[var1++] << 16) & 16711680L | (long)(var0[var1] << 24) & 4278190080L;
        }

        public static int getInt3(byte[] var0, int var1) {
            return var0[var1++] & 255 | var0[var1++] & '\uff00' | var0[var1] & 16711680;
        }

        public static int getInt2(byte[] var0, int var1) {
            return var0[var1++] & 255 | var0[var1] & '\uff00';
        }

        public static int getInt1(byte[] var0, int var1) {
            return var0[var1] & 255;
        }

        public static long ip2long(String var0) {
            String[] var1 = var0.split("\\.");
            if (var1.length != 4) {
                return 0L;
            } else {
                int var2 = Integer.valueOf(var1[0]) << 24 & -16777216;
                int var3 = Integer.valueOf(var1[1]) << 16 & 16711680;
                int var4 = Integer.valueOf(var1[2]) << 8 & '\uff00';
                int var5 = Integer.valueOf(var1[3]) << 0 & 255;
                return (long)(var2 | var3 | var4 | var5) & 4294967295L;
            }
        }

        public static String long2ip(long var0) {
            StringBuilder var2 = new StringBuilder();
            var2.append(var0 >> 24 & 255L).append('.').append(var0 >> 16 & 255L).append('.').append(var0 >> 8 & 255L).append('.').append(var0 >> 0 & 255L);
            return var2.toString();
        }

        public static boolean isIpAddress(String var0) {
            String[] var1 = var0.split("\\.");
            if (var1.length != 4) {
                return false;
            } else {
                String[] var2 = var1;
                int var3 = var1.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    String var5 = var2[var4];
                    if (var5.length() > 3) {
                        return false;
                    }

                    int var6 = Integer.valueOf(var5);
                    if (var6 > 255) {
                        return false;
                    }
                }

                return true;
            }
        }
    }


}


