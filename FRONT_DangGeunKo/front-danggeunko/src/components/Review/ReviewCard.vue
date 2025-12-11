<template>
    <div>
         <div class="c-item">
            <div class="c-top">
                <div class="c-user">
                    <img src="" />
                    {{ review.nickName }}
                </div>
                <div class="c-meta">{{ review.region }} · {{review.updatedAt}}</div>
            </div>
            <div class="star-show">{{ review.rating }}</div>
            <div class="c-content">{{ review.content }}</div>
            <!-- 본인일 경우만 노출 -->
            <div style="margin-left:36px;margin-top:4px;">
                <button class="edit-btn">수정</button>
                <button class="del-btn">삭제</button>
            </div>
        </div>
    </div>
</template>

<script setup>
    const props = defineProps({
        review: Object,
    })
</script>

<style  scoped>

</style>