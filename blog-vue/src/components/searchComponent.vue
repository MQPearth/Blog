<template>  
    <div class="search-container">  
      <input  
        type="text"  
        class="search-input"  
        v-model="searchText"  
        placeholder="Search..."  
        @input="onSearch"  
      />  
      <button class="search-button" @click="onSearchClick">  
        <i class="fa fa-search"></i>  
      </button>  
    </div>  
  </template>  
    
  <script>  
  export default {  
    data() {  
      return {  
        searchText: ""  
      };  
    },  
    methods: {  
      onSearch() {  
        this.$emit("search", this.searchText);  
      },  
      onSearchClick() {  
        this.onSearch();  
      }  
    }  
  };  
  </script>  
    
  <style scoped>  
  .search-container {  
    position: relative;  
  }  
    
  .search-input {  
    width: 100%;  
    padding: 10px;  
    border: 1px solid #ccc;  
    border-radius: 4px;  
    font-size: 16px;  
  }  
    
  .search-button {  
    position: absolute;  
    top: 0;  
    right: 0;  
    background-color: #4CAF50;  
    color: white;  
    border: none;  
    border-radius: 0 4px 4px 0;  
    cursor: pointer;  
    padding: 10px;  
  }  
  </style>