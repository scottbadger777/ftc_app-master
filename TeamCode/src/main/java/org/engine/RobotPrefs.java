package org.engine;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
/* Created by t420 on 9/29/2016. */

//Setup robot preferences
public abstract class RobotPrefs extends OpMode {

    /* ************* NAME YOUR CONFIG FILES THE SAME AS IN ROBOT PREFS ************/
    final String TAG = "program.robotprefs";

    /* EXAMPLES  of device type and names */
    /*
    public volatile DeviceInterfaceModule deviceInterfaceModule;
    public volatile ColorSensor colorSensorRight;
    public volatile DcMotor dcBackLeft;
    public volatile I2cDevice distanceSensorFront;
    public volatile TouchSensor shooterTouch;
    public volatile CRServo svRightFront;
    public volatile Servo svRightBack;
    */

    public volatile DcMotor[] motors = new DcMotor[8];

    public static final int RANGE1_REG_START = 0x04; //Register to start reading
    public static final int RANGE1_READ_LENGTH = 2;

    public DcMotor getMotor(int motor){
        /* setup an array of motor names*/
        /* example
        motors[0] = dcBackLeft;
        motors[1] = dcBackRight;
        motors[2] = dcFrontLeft;
        motors[3] = dcFrontRight;
        motors[4] = dcPresser;
        motors[5] = dcShooter;
        */

        Log.i(TAG,Integer.toString(motor));
        return motors[motor];
    }
}