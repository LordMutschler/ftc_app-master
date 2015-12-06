package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Infant Derrick on 11/15/2015.
 */
public class ABbotManualRun extends PushBotTelemetry{

    public ABbotManualRun ()

    {

        //
        // Initialize base classes.
        //p
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotManual

    public void update_telemetry () {
        super.update_telemetry();
        telemetry.addData( "23", "Debug Variable Long: " + getDebugVarLong());
        telemetry.addData( "21", "Arm Position: " + left_arm_encoder_count());
        telemetry.addData( "24", "Debug Variable Double: " + getDebugVarDouble());
        telemetry.addData("22", "Arm Speed: " + getAveRightSpeed());
    }
    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during
     * manual-operation.  The state machine uses gamepad input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        int CurrentPos = left_arm_encoder_count();
        long CurrentTime = System.currentTimeMillis ();

        RightArmSpeed = (double)(CurrentPos-PreviousPos)/(CurrentTime-PreviousTime);
        AveRightSpeed = (3*AveRightSpeed + RightArmSpeed)/4;
        debugVarLong = CurrentTime-PreviousTime;
        debugVarDouble = (double)(CurrentPos-PreviousPos);
        PreviousPos = CurrentPos;
        PreviousTime = CurrentTime;

       
        //----------------------------------------------------------------------
        //
        // DC Motors
        //
        // Obtain the current values of the joystick controllers.
        //
        // Note that x and y equal -1 when the joystick is pushed all of the way
        // forward (i.e. away from the human holder's body).
        //
        // The clip method guarantees the value never exceeds the range +-1.
        //
        // The DC motors are scaled to make it easier to control them at slower
        // speeds.
        //
        // The setPower methods write the motor power values to the DcMotor
        // class, but the power levels aren't applied until this method ends.
        //

        //
        // Manage the drive wheel motors.
        //
        float l_left_drive_power = scale_motor_power (gamepad1.left_stick_y);
        float l_right_drive_power = scale_motor_power (gamepad1.right_stick_y);

        set_drive_power (l_left_drive_power, l_right_drive_power);

        //
        // Manage the arm motor.
        //
        if (gamepad2.dpad_down){
             v_servo_left_hand.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad2.dpad_up){
            v_servo_left_hand.setPosition(Servo.MAX_POSITION);
        }


        float l_left_arm_power = scale_motor_power (gamepad2.left_stick_y / 2);
            m_left_arm_power (l_left_arm_power);

        float l_arm_extender_power = scale_motor_power(-gamepad2.right_stick_y);
            m_arm_extender(l_arm_extender_power);
        //----------------------------------------------------------------------
        //
        // Servo Motors
        //
        // Obtain the current values of the gamepad 'x' and 'b' buttons.
        //
        // Note that x and b buttons have boolean values of true and false.
        //
        // The clip method guarantees the value never exceeds the allowable range of
        // [0,1].
        //
        // The setPosition methods write the motor power values to the Servo
        // class, but the positions aren't applied until this method ends.
        //
        if (gamepad2.x)
        {
            m_hand_position (a_hand_position () + 0.05);
        }
        else if (gamepad2.b)
        {
            m_hand_position (a_hand_position () - 0.05);
        }

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        update_gamepad_telemetry ();

    } // loop


    public double getDebugVarDouble() {

        return debugVarDouble;
    }

    public long getDebugVarLong() {
        return debugVarLong;
    }
    public double getAveRightSpeed ()
    {
        return AveRightSpeed;

    } // a_left_encoder_count
    public double getRightArmSpeed ()
    {
        return RightArmSpeed;
    } // a_left_encoder_count
    public int PreviousPos ;//= 0 ;//
    public long PreviousTime;// = 0;
    public double RightArmSpeed;
    public double debugVarDouble;
    public long debugVarLong;
    public double AveRightSpeed;

}
