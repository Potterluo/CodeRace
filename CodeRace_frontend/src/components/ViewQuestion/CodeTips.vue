<template>
  <div id="codeTips">
    <a-modal v-model:visible="isShow" @cancel="colseTips" @close="colseTips">
      <template #title>
        <div class="header-txt">
          <span>题目输入输出处理</span>
        </div>
      </template>
      <div>
        <div class="desc-txt">1. 核心代码模式处理</div>
        <div class="desc-txt">不需要关心输入问题， 按照题目要求打印输出结果即可。</div>
        <div class="desc-txt">2. ACM 模式</div>
        <div class="desc-txt">
          你的代码需要处理输入输出，请使用如下样例代码读取输入和打印输出：
        </div>
        <MdViewer :value="codeExample" />
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import MdViewer from "@/components/markdown/MdViewer.vue";

/**
 * 定义组件属性类型
 */
interface Props {
  visible: boolean;
  colseTips: () => void;
}

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  visible: () => false,
  colseTips: () => {
    console.log("关闭提示");
  },
});

const isShow = computed(() => props.visible);

const codeExample = ref(
  "```java\n"+
  "public class Main {\n" +
  "    public static void main(String[] args) {\n" +
  "        int a = Integer.parseInt(args[0]);\n" +
  "        int b = Integer.parseInt(args[1]);\n" +
  "        System.out.println(a + b);\n" +
  "    }\n" +
  "}\n"+
  "```"
);
</script>

<style scoped>
:deep(.arco-modal-header) {
  justify-content: flex-start;
}
.header-txt {
  font-size: 14px;
  font-weight: 400;
}
.desc-txt {
  margin-bottom: 5px;
  font-size: 12px;
}
</style>
