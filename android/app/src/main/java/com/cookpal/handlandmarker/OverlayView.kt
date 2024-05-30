/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cookpal.handlandmarker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

import com.cookpal.R
import com.cookpal.Global
import com.cookpal.handlandmarker.HandPose

import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

import kotlin.math.max
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results: HandLandmarkerResult? = null
    private var linePaint = Paint()
    private var pointPaint = Paint()

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    init {
        initPaints()
    }

    fun clear() {
        results = null
        linePaint.reset()
        pointPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        linePaint.color =
            ContextCompat.getColor(context!!, R.color.mp_color_primary)
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL
    }


    fun calculateHandPose() {
        val finger = arrayOf(intArrayOf(5, 6, 8), intArrayOf(9, 10, 12), intArrayOf(13, 14, 16), intArrayOf(17, 18, 20), intArrayOf(0, 2, 4))
        //val finger = arrayOf(intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 16), intArrayOf(17, 18, 19, 20), intArrayOf(1, 2, 3, 4))
        var kindOfHand = 0;
        results?.let { handLandmarkerResult ->

            for (handside in handLandmarkerResult.handednesses()){
                    kindOfHand = handside.get(0).index()
                    if (kindOfHand == 0) kindOfHand = -1 
            }
            
            var isFist = 1
            var isVictory = 1
            
            val threshold = 0.8f
            val thumbthreshold = 0.5f
            var cos: Double = 0.0

            var a = DoubleArray(3)
            var b = DoubleArray(3)
            var aLen: Double
            var bLen: Double

            for (landmark in handLandmarkerResult.landmarks()) {

                if(landmark.get(17).x()<landmark.get(5).x()){
                    Global.backOrFront = kindOfHand*-1
                }else{
                    Global.backOrFront = kindOfHand
                }

                for (i in 0..4) {
                    aLen = 0.0
                    bLen = 0.0
                    cos = 0.0
                    
                    a[0] = (landmark.get(finger[i][0]).x() - landmark.get(finger[i][1]).x()).toDouble()
                    a[1] = (landmark.get(finger[i][0]).y() - landmark.get(finger[i][1]).y()).toDouble()
                    a[2] = (landmark.get(finger[i][0]).z() - landmark.get(finger[i][1]).z()).toDouble()

                    b[0] = (landmark.get(finger[i][2]).x() - landmark.get(finger[i][1]).x()).toDouble()
                    b[1] = (landmark.get(finger[i][2]).y() - landmark.get(finger[i][1]).y()).toDouble()
                    b[2] = (landmark.get(finger[i][2]).z() - landmark.get(finger[i][1]).z()).toDouble()
                     
                    /*
                    a[0] = (landmark.get(finger[i][0]).x() - landmark.get(finger[i][1]).x()).toDouble()
                    a[1] = (landmark.get(finger[i][0]).y() - landmark.get(finger[i][1]).y()).toDouble()
                    a[2] = (landmark.get(finger[i][0]).z() - landmark.get(finger[i][1]).z()).toDouble()

                    b[0] = (landmark.get(finger[i][3]).x() - landmark.get(finger[i][2]).x()).toDouble()
                    b[1] = (landmark.get(finger[i][3]).y() - landmark.get(finger[i][2]).y()).toDouble()
                    b[2] = (landmark.get(finger[i][3]).z() - landmark.get(finger[i][2]).z()).toDouble()
                    */
                    for (j in 0..2) {
                        aLen += a[j] * a[j]
                        bLen += b[j] * b[j]
                        cos += a[j] * b[j]
                    }
                    aLen = kotlin.math.sqrt(aLen)
                    bLen = kotlin.math.sqrt(bLen)

                    cos = cos / (aLen * bLen)
                    if(i==0){Global.test1 = cos}
                    if(i==1){Global.test2 = cos}
                    if(i==2){Global.test3 = cos}
                    if(i==3){Global.test4 = cos}
                    if(i==4){Global.test5 = cos}

                    
                    
                    if (cos < 0 && kotlin.math.abs(cos)>= threshold) {
                        isFist = 0
                        if (i >= 2) isVictory = 0
                    }else if(i<2){
                        isVictory = 0
                    }
                    
                    if (isFist==0 && isVictory==0) break
                }

                Global.isFist = isFist
                Global.isVictory = isVictory
                
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { handLandmarkerResult ->
            calculateHandPose()
            for (landmark in handLandmarkerResult.landmarks()) {
                for (normalizedLandmark in landmark) {
                    canvas.drawPoint(
                        normalizedLandmark.x() * imageWidth * scaleFactor,
                        normalizedLandmark.y() * imageHeight * scaleFactor,
                        pointPaint
                    )
                }
                //Global.test = landmark.get(0).x()
                HandLandmarker.HAND_CONNECTIONS.forEach {
                    canvas.drawLine(
                        landmark.get(it!!.start())
                            .x() * imageWidth * scaleFactor,
                        landmark.get(it.start())
                            .y() * imageHeight * scaleFactor,
                        landmark.get(it.end())
                            .x() * imageWidth * scaleFactor,
                        landmark.get(it.end())
                            .y() * imageHeight * scaleFactor,
                        linePaint
                    )
                }
            }
        }
    }

    fun setResults(
        handLandmarkerResults: HandLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        results = handLandmarkerResults

        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }
            RunningMode.LIVE_STREAM -> {
                // PreviewView is in FILL_START mode. So we need to scale up the
                // landmarks to match with the size that the captured images will be
                // displayed.
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 8F
    }
}
