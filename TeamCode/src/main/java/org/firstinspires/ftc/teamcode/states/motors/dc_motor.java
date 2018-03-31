package org.firstinspires.ftc.teamcode.states.motors;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.engine.Engine;
import org.engine.State;

public class dc_motor extends State{
    private DcMotor scott_motor_one;
    public dc_motor (Engine scott_engine){
        this.engine=scott_engine;
    }

    @Override
    public void init(){
        scott_motor_one = this.engine.hardwareMap.dcMotor.get("the_first_motor");
    }

    @Override
    public void exec() {
        scott_motor_one.setPower((double) 1.0);
    }
}
