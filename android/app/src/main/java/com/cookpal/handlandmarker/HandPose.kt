package com.cookpal.handlandmarker

import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.cookpal.Global

public class HandPose {
    // 집게, 중지, 약지, 새끼, 엄지
    //private val finger = arrayOf(intArrayOf(5, 6, 7), intArrayOf(9, 10, 11), intArrayOf(13, 14, 15), intArrayOf(17, 18, 19), intArrayOf(2, 3, 4))
    private val finger = arrayOf(intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 16), intArrayOf(17, 18, 19, 20), intArrayOf(1, 2, 3, 4))
    public fun calculateHandPose(handLandmarkerResult: HandLandmarkerResult) {
        var kindOfHand = 0;
        for (handside in handLandmarkerResult.handednesses()){
                kindOfHand = handside.get(0).index()
                if (kindOfHand == 0) -1 
        }
        
        var isFist = 1
        var isVictory = 1

        val threshold = 0.7f
        //val thumbThreshold = 0.5f
        var cos = 0f

        var a = FloatArray(3)
        var b = FloatArray(3)
        var aLen: Float
        var bLen: Float

        for (landmark in handLandmarkerResult.landmarks()) {
            for (i in 0..4) {
                aLen = 0f
                bLen = 0f
                cos = 0f
                /*
                a[0] = landmark.get(finger[i][0]).x() - landmark.get(finger[i][1]).x()
                a[1] = landmark.get(finger[i][0]).y() - landmark.get(finger[i][1]).y()
                a[2] = landmark.get(finger[i][0]).z() - landmark.get(finger[i][1]).z()

                b[0] = landmark.get(finger[i][2]).x() - landmark.get(finger[i][1]).x()
                b[1] = landmark.get(finger[i][2]).y() - landmark.get(finger[i][1]).y()
                b[2] = landmark.get(finger[i][2]).z() - landmark.get(finger[i][1]).z()
                 */

                a[0] = landmark.get(finger[i][0]).x() - landmark.get(finger[i][1]).x()
                a[1] = landmark.get(finger[i][0]).y() - landmark.get(finger[i][1]).y()
                a[2] = landmark.get(finger[i][0]).z() - landmark.get(finger[i][1]).z()

                b[0] = landmark.get(finger[i][3]).x() - landmark.get(finger[i][2]).x()
                b[1] = landmark.get(finger[i][3]).y() - landmark.get(finger[i][2]).y()
                b[2] = landmark.get(finger[i][3]).z() - landmark.get(finger[i][2]).z()

                for (j in 0..2) {
                    aLen += a[j] * a[j]
                    bLen += b[j] * b[j]
                    cos += a[j] * b[j]
                }
                aLen = kotlin.math.sqrt(aLen)
                bLen = kotlin.math.sqrt(bLen)

                cos = kotlin.math.abs(cos) / (aLen * bLen)

                //if (i < 4) {
                    if (cos >= threshold) {
                        isFist = 0
                        if (i < 2) isVictory = 0
                    }
                /*} else {
                    if (cos >= thumbThreshold) {
                        isFist = 0
                        if (i < 2) isVictory = 0
                    }
                }*/
                if (isFist==0 && isVictory==0) break
            }
            Global.isFist = isFist
            Global.isVictory = isVictory
        }
    }
}

