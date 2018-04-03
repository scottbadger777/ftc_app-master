package org.firstinspires.ftc.teamcode.states.motors;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.engine.Engine;
import org.engine.State;

/**
 * Created by g023985 on 4/2/2018.
 */

public class ScottsFirstMotor extends State{
    public ScottsFirstMotor (Engine scott_engine){this.engine=scott_engine;} // class scoped to engine
    private DcMotor pToMotor; // pointer to motor
    private String szMotorName = "mc1p2"; // name on phone for motor in port 2

    @Override
    public void init(){
        pToMotor = this.engine.hardwareMap.dcMotor.get(szMotorName); // set pointer from hardware map
    }

    @Override
    public void exec(){
        pToMotor.setPower((double) 1.0); // drive the motor full on
    }
}
