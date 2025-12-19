import { ref } from 'vue'

const panelState = ref(0)
// 0: hidden, 1: half, 2: full

const TOP_RESERVED = 120

let startY = 0
let startHeight = 0
let dragging = false
let activePanel = null
let tapCount = 0;

const getHeights = () => {
  const usable = window.innerHeight - TOP_RESERVED
  return {
    hidden: 0,
    half: (usable / 3)*2,
    full: usable
  }
}

/* =====================
   탭 토글
===================== */
const toggleByTap = () => {
  tapCount++

  if (tapCount % 4 === 1) panelState.value = 1 // half
  else if (tapCount % 4 === 2) panelState.value = 2 // full
  else if (tapCount % 4 === 3) panelState.value = 1 // half
  else panelState.value = 0 // hidden
}

const syncTapCount = (state) => {
  if (state === 1) tapCount = 1;      // half 상태면 1번 탭한 걸로 간주
  else if (state === 2) tapCount = 2; // full 상태면 2번 탭한 걸로 간주
  else if (state === 0) tapCount = 0; // hidden 상태면 0번 탭한 걸로 간주
}

/* =====================
   높이 적용
===================== */
const applyHeight = (panelEl) => {
  if (!panelEl) return

  const { hidden, half, full } = getHeights()

  panelEl.style.display =
    panelState.value === 0 ? 'none' : 'block'

  if (panelState.value === 0) panelEl.style.height = `${hidden}px`
  if (panelState.value === 1) panelEl.style.height = `${half}px`
  if (panelState.value === 2) panelEl.style.height = `${full}px`
}

/* =====================
   드래그 시작
===================== */
const startDrag = (e, panelEl) => {
  dragging = true
  activePanel = panelEl
  startY = e.clientY
  startHeight = panelEl.offsetHeight

  document.addEventListener('mousemove', onMouseMove)
  document.addEventListener('mouseup', stopDrag)
}

/* =====================
   드래그 중
===================== */
const onMouseMove = (e) => {
  if (!dragging || !activePanel) return

  const diff = startY - e.clientY
  let newHeight = startHeight + diff

  const { hidden, full } = getHeights()
  newHeight = Math.max(hidden, Math.min(newHeight, full))

  activePanel.style.height = `${newHeight}px`
}

/* =====================
   드래그 종료 + 스냅
===================== */
const stopDrag = () => {
  if (!activePanel) return

  dragging = false

  const h = activePanel.offsetHeight
  const { hidden, half, full } = getHeights()

  const candidates = [
    { state: 0, value: hidden },
    { state: 1, value: half },
    { state: 2, value: full }
  ]

  candidates.sort(
    (a, b) => Math.abs(h - a.value) - Math.abs(h - b.value)
  )

  panelState.value = candidates[0].state

  document.removeEventListener('mousemove', onMouseMove)
  document.removeEventListener('mouseup', stopDrag)

  activePanel = null
}

const setPanelState = (newState) => {
    panelState.value = newState
  }

export const useSlidingPanel = () => {
  return {
    panelState,
    setPanelState,
    toggleByTap,
    applyHeight,
    startDrag,
    syncTapCount,
  }
}