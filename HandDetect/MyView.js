import React, {useEffect, useRef, useState} from 'react';
import {PixelRatio, UIManager, findNodeHandle, Text, View, StyleSheet} from 'react-native';
import { useNavigation } from '@react-navigation/native';

import {PermissionManager} from './PermissionManager';
import {CameraViewManager} from './CameraManager';
import {HandControl} from'./HandControl';
import {VoiceControl} from'./VoiceControl';
import {VoiceCommend} from'./VoiceCommend';

// 카메라 화면을 위해 설정
const createFragment = viewId =>
  UIManager.dispatchViewManagerCommand(
    viewId,
    UIManager.CameraViewManager.Commands.create.toString(),
    [viewId],
);

const removeFragment = viewId =>
  UIManager.dispatchViewManagerCommand(
    viewId,
    UIManager.CameraViewManager.Commands.remove.toString(),
    [viewId],
);

const voiceCom = new VoiceCommend()
async function createCommend(){
  try{
    await voiceCom._makeManager()
  }catch(error){
    console.log(error)
  }
}

export default Recipe_view = () => {
  const ref = useRef(null);
  //핸드 컨트롤을 위한 객체 선언
  const handCon = new HandControl();
  const navigation = useNavigation();
  let [pageNum, pageCalculate] = useState(0);
  let [isCall, callCookPal] = useState(false);
  let [commend, getCommend] = useState("");
  let [ismove, handmove] = useState(0);
  
  useEffect(() => {
    
    //보이스 컨트롤을 위한 객체 및 비동기 함수 선언
    const voiceCon = new VoiceControl(callCookPal);
    async function createVoice(){
      try{
        await voiceCon._makeManager()
        await voiceCon._startProcessing()
      }catch(error){
        console.log(error);
      }
    }
    createVoice();
    createCommend();

    //핸드 제스쳐 화면 보여주기 위함
    const viewId = findNodeHandle(ref.current);

    createFragment(viewId);
    
    //View가 사라지기 전에 동작
    const beforeRemoveListener = navigation.addListener('beforeRemove', (e) => {
      removeFragment(viewId);
    });
    return() => {
      beforeRemoveListener();
      voiceCom.componentWillUnmount();
      voiceCon.componentWillUnmount();}
  }, []);

  /*useEffect(() => {
    const interval = setInterval(()=>{
      handCon.calculate_hand_move()
      if(handCon.fin==1){
        pageCalculate(handCon.page_num);
        console.log(handCon.page_num)
        handCon.sequence = 0;
        handCon.fin = 0;
      }
    }, 1000);
    return() => (clearInterval(interval))
  }, []);*/

  useEffect(() => {
    let isMounted = true; // 컴포넌트 마운트 여부 확인
    let count = 0;
    const performHandMove = async () => {
      if (!isMounted) return; // 컴포넌트가 마운트된 경우에만 수행

      try {
        await handCon.calculate_hand_move();
        if (handCon.fin == 1) {
          pageCalculate(handCon.page_num);
          handCon.sequence = 0;
          handCon.fin = 0;
          count = 0
        }
        count++
        if(count>20){ //2초 동안 움직임의 변화가 없으면 초기화
          handCon.sequence = 0;
          count = 0
        }
      } catch (error) {
        console.error(error);
      }

      if (isMounted) {
        // 0.1초 후에 다시 수행
        setTimeout(performHandMove, 100);
      }
    };

    // 초기 실행
    performHandMove();

    return () => {
      isMounted = false; // 컴포넌트 언마운트 시 플래그를 false로 설정
    };
  }, []); 


  /*
  useEffect(() => {
    if (ismove>0) {
      console.log("+++++++++++++")
      const seq_init = setTimeout(() => {
        console.log("---------------")
        handCon.sequence = 0
        handmove(0)
      }, 3000);
      return () => clearTimeout(seq_init);
    }
  }, [ismove]);
*/
  
  useEffect(() => {
    // isCall이 true라면 isCall false로
    // 일정 시간이 지나도 안불리면 자동으로 종료되기 위함
    if (isCall) {
      voiceCom._startProcessing(getCommend)
      const timerId = setTimeout(() => {
        callCookPal(false)
        getCommend("")
      }, 5000);
      return () => clearTimeout(timerId);
    }
  }, [isCall]);

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Text style={styles.blackText}>
        {pageNum}
      </Text>
        <View>
          <PermissionManager
            style={{
              height: PixelRatio.getPixelSizeForLayoutSize(20),
              width: PixelRatio.getPixelSizeForLayoutSize(20),
            }}
            ref={ref}
          />

          <CameraViewManager
            style={{
              height: PixelRatio.getPixelSizeForLayoutSize(100),
              width: PixelRatio.getPixelSizeForLayoutSize(100),
            }}
            ref={ref}
          />
        </View>
      {isCall ? <Text style={styles.blackText}>cookpal 불림</Text> : <Text>...</Text>}
      {<Text style={styles.blackText}>{commend}</Text>}
    </View>
    
  );};

  const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
    },
    blackText: {
      color: 'black',
    },
  });

