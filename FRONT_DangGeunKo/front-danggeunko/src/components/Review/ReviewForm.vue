<template>
    <div>
        <div class="stats">
            <div><span class="heart" id="heart">♡</span> 99</div>
            <div>💬 10</div>
            <div>👁 3,202</div>
        </div>
        <!-- 댓글 입력 -->
        <div class="comment-box">
            <textarea class="comment-input" placeholder="코스에 대한 리뷰를 남겨주세요."></textarea>

            <div class="stars" id="starRate">
                <span data-v="1">★</span>
                <span data-v="2">★</span>
                <span data-v="3">★</span>
                <span data-v="4">★</span>
                <span data-v="5">★</span>
            </div>

            <button class="comment-submit">등록</button>
        </div>
    </div>
</template>

<script setup>

</script>

<style scoped>
/* 좋아요 + 댓글 + 조회수 */
    .stats{
        display:flex; align-items:center; gap:14px; margin-bottom:20px;
    }
    .stats div{ font-size:13px; display:flex; align-items:center; gap:4px;}
    .heart{ color:#aaa; cursor:pointer; font-size:18px;}
    .heart.active{ color:#ff7a00;}

    /* 댓글 입력칸 */
    .comment-box{
        padding:12px; background:#fff; border-top:1px solid #eee; border-bottom:1px solid #eee;
        margin-bottom:16px;
    }
    .comment-input{
        width:95%; padding:10px; border-radius:10px; border:1px solid #ddd; font-size:12px;
        margin-bottom:10px;
    }

    /* 별점 */
    .stars span{
        font-size:20px; cursor:pointer; color:#ddd;
    }
    .stars span.active{ color:#ffb400; }

    .comment-submit{
        background:#ff7a00; color:#fff; width:100%; border:none; height:36px;
        border-radius:10px; font-size:13px; font-weight:600; cursor:pointer; margin-top:8px;
    }
</style>