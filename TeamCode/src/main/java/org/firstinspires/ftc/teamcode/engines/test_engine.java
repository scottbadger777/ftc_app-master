package org.firstinspires.ftc.teamcode.engines;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.engine.Engine;
import org.firstinspires.ftc.teamcode.states.motors.dc_motor;

@Autonomous(name = "scott_autonomous")

public class test_engine extends Engine{
    @Override
    public void setProcesses() {
        addState(new dc_motor(this));

    }
}
