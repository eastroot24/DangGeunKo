<template>
    <div class="top-search-row">
        <div>
              <select class="category-btn" id="categorySelect">
                <option value="courseName">제목</option>
                <option value="userName">작성자</option>
                <option value="content">내용</option>
              </select>
          </div>

        <div class="search-input-wrap">
          <input
            type="text"
            class="search-input"
            placeholder="코스 제목을 입력하세요"
          />
          <span class="search-icon">🔍</span>
        </div>
      </div>
</template>

<script setup>

</script>

<style scoped>

</style>