import {
  NativeModules,
} from 'react-native';

const {SaveModule} = NativeModules;

export class HandControl{
    // 손 시퀀스 연산에 필요한 변수들 저장
    constructor() {
        this.page_num = 0;
        this.direction = 0;
        this.sequence = 0;
        this.fin = 0;
        this.flag = 0;
    }

    get_check(){
      SaveModule.getCheck((state) =>{
        console.log("check : "+state);
      });
    }
    get_hand_state(){
        SaveModule.getTest1((state) =>{
            //console.log("test1 : "+state);
          });
          SaveModule.getTest2((state) =>{
            //console.log("test2 : "+state);
          });
          SaveModule.getTest3((state) =>{
            //console.log("test3 : "+state);
          });
          SaveModule.getTest4((state) =>{
            //console.log("test4 : "+state);
          });
          SaveModule.getTest5((state) =>{
            //console.log("test5 : "+state);
          }); 
          SaveModule.getFist((state) =>{
            console.log("fist : "+state);
          });
          SaveModule.getVictory((state) =>{
            console.log("victory : "+state);
          });
          SaveModule.getBackOrFront((state)=>{
            //console.log("BackOrFront : "+ state);
          });
    }
    
    //손 시퀀스 인식
    // victory -> fist -> victory
    calculate_hand_move(){
      if (this.sequence == 0) {//처음 victory 인식
        SaveModule.getVictory(state => {
          if (state) {
            this.flag = 1;
            this.sequence++;
            SaveModule.getBackOrFront(state => {
                this.direction = state;
            });
          }
        });
      }else if (this.sequence == 1) {//이전에 victory가 인식 된적 있음
        SaveModule.getFist(state => {//손 모양이 fist
          if (state) {
            SaveModule.getBackOrFront(state => {
              if (state == this.direction) {//victory 일때와 동일한 방향의 손
                this.sequence++;
                this.flag = 1;
              } else {//손 모양은 주먹이지만 손 방향이 반대
                this.sequence = 0;
                this.count = 0;
              }
            });
          }
        });
      }else {
        SaveModule.getVictory(state => {//주먹 이후 victory
          if (state) {
            SaveModule.getBackOrFront(state => {
              if (this.direction == state) {//손 방향 동일
                if (this.direction == 1) {
                    this.page_num++;
                } else {
                    this.page_num--;
                }
              }
              this.sequence = 0;
              this.fin = 1;
            });
          }
        });
      }
      //console.log("++++++++++++++++++++++++")
    }
}

