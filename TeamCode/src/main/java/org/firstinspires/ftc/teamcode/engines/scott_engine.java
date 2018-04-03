package org.firstinspires.ftc.teamcode.engines;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.engine.Engine;
import org.firstinspires.ftc.teamcode.states.motors.ScottsFirstMotor;

@Autonomous(name = "scott_auto_motor_controller") // set the name to what you will see on the screen

public class scott_engine extends Engine {
    @Override
    public void setProcesses() {
        addState(new ScottsFirstMotor(this)); // run the motor at top speed
    }
}
