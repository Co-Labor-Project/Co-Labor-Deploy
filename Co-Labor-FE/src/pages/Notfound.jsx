import styled from 'styled-components';
const Notfound = () => {
  return (
    <BackGround>
      <Text404>4 0 4</Text404>
      <Text>Page Notfound</Text>
      <Text>잘못된 접근입니다</Text>
    </BackGround>
  );
};
export default Notfound;

const BackGround = styled.div`
  display: flex;
  height: 95vh;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 30px;
`;

const Text404 = styled.div`
  font-size: 160px;
  font-weight: 800;
  background: linear-gradient(to right top, #861657, #f48378);
  color: transparent;
  -webkit-background-clip: text;
`;
const Text = styled.div`
  font-size: 50px;
  font-weight: 800;
  background: linear-gradient(to right top, #861657, #f48378);
  color: transparent;
  -webkit-background-clip: text;
`;
