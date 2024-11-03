// 검색기록이 비어있는지 안 비어있는지 확인하는 훅
const useEmpty = (obj) => {
  if (obj.constructor === Object && Object.keys(obj).length === 0) {
    return true;
  }
  return false;
};

export default useEmpty;
