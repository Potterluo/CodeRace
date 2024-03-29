<template>
  <a-row id="quesrionHeader" align="center" :wrap="false">
    <a-col :span="4">
      <div class="title-bar" @click="doTitleClick">
        <img src="@/assets/logo.png" v-if="theme === 'light'" style="height: 50px"/>
        <img src="@/assets/logo-dark.png" v-else style="height: 50px"/>
      </div>
    </a-col>
    <a-col :span="8" :offset="6">
      <div class="start">
        <a-space size="medium">
        </a-space>
      </div>
    </a-col>
    <a-col :span="4" :offset="2">
      <div class="end">
        <a-space size="medium">
          <LightSetting />
          <div class="startTimer" v-if="!showTimer" @click="startTimer">
            <a-tooltip position="bottom" content="开始计时" mini>
              <icon-clock-circle
                :style="{
                  fontSize: '22px',
                  color: '#0A65CC',
                  marginTop: '2px',
                }"
              />
            </a-tooltip>
          </div>
          <div class="stopTimer" v-if="showTimer" @click="stopTimer">
            <div class="timerText">{{ formatTime(time) }}</div>
            <icon-loop class="stopTimerIcon" :style="{ color: '#0A65CC' }" />
          </div>
          <userAvatar />
        </a-space>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import userAvatar from "@/components/setting/userAvatar.vue";
import { QuestionControllerService } from "../../../generated";
import { ref, watch, onUnmounted, computed } from "vue";
import { Message } from "@arco-design/web-vue";
import "@arco-design/web-vue/es/message/style/css.js";
import { useStore } from "vuex";
import {
  IconLoop,
  IconClockCircle,
  IconRight,
  IconLeft,
} from "@arco-design/web-vue/es/icon";
import LightSetting from "@/components/setting/LightSetting.vue";

const store = useStore();
const theme = computed(() => store.state.theme.theme);
const showTimer = ref(false);
const time = ref(0);
let intervalId: any = null;
const router = useRouter();
const route = useRoute();
const user = store.state.user;
const questionId = ref(route.params.id);
watch(route, () => {
  questionId.value = route.params.id as string;
});

const doTitleClick = () => {
  router.push({
    path: "/",
    replace: true,
  });
};
const getPrevQuestion = async () => {
  const res = await QuestionControllerService.getPreviousQuestionUsingGet(
    questionId.value as any
  );
  if (res.message === "OK") {
    router.push({ path: `/view/question/${res.data?.id}` });
  } else {
    Message.error("已经是第一道题目啦！");
  }
};

const getNextQuestion = async () => {
  const res = await QuestionControllerService.getNextQuestionUsingGet(
    questionId.value as any
  );
  if (res.message === "OK") {
    router.push({ path: `/view/question/${res.data?.id}` });
  } else {
    Message.error("已经是最后的一道题目啦！");
  }
};
const matchQuestion = async () => {
  const res = await QuestionControllerService.matchQuestionVoUsingGet(
    questionId.value as any
  );
  if (res.message === "OK") {
    router.push({ path: `/view/question/${res.data?.id}` });
  } else {
    Message.error("error");
  }
};
const formatTime = (time: number): string => {
  const hours = Math.floor(time / 3600);
  const minutes = Math.floor((time % 3600) / 60);
  const seconds = time % 60;

  return `${hours < 10 ? "0" + hours : hours}:${
    minutes < 10 ? "0" + minutes : minutes
  }:${seconds < 10 ? "0" + seconds : seconds}`;
};

const startTimer = () => {
  showTimer.value = true;
};
const stopTimer = () => {
  showTimer.value = false;
  time.value = 0;
};

watch(
  () => showTimer.value,
  (newVal: boolean) => {
    if (newVal) {
      intervalId = setInterval(() => {
        time.value++;
      }, 1000);
    } else {
      clearInterval(intervalId);
    }
  }
);

onUnmounted(() => {
  clearInterval(intervalId);
});
</script>

<style scoped>
#quesrionHeader {
  height: 40px;
  padding: 0 20px;
  border-bottom: 1px solid #cdcdcd;
}
.title-bar {
  cursor: pointer;
}
:deep(.arco-menu-horizontal .arco-menu-inner) {
  padding: 0px 25px;
}
.start {
  display: flex;
}
.random {
  margin-top: 2px;
  cursor: pointer;
}
.end {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-right: 20px;
}

.startTimer {
  padding: 5px;
  font-size: 20px;
  cursor: pointer;
}
.startTimer:hover {
  background-color: var(--color-neutral-3);
}
.stopTimer {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(10, 101, 204, 0.15);
  border-radius: 5px;
  padding: 5px 10px;
}
.stopTimerIcon {
  font-size: 20px;
  cursor: pointer;
  margin-top: "2px";
}
.timerText {
  color: var(--color-text-1);
}
</style>
