package org.usfirst.frc.team340.robot.commands;

import java.util.logging.Logger;

import org.usfirst.frc.team340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArmToPosition extends Command {
	
	//Variables
	public double cmdSpeed;
	public double currPos;
	public double tgtPos;
	public double potAverage = (Robot.harvester.getLeftAimPot() + Robot.harvester.getRightAimPot()) / 2;
	public double ThV = 0;
	public double maxAcc = 0.05;  //Increases speed every tick. This will be changed possibly, not sure.
	public double vBound = 1;
	
	//Logger
	Logger logger = Robot.getLogger(ArmToPosition.class);
	
    public ArmToPosition(double tgtPos) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	requires(Robot.harvester);
    	this.tgtPos = tgtPos;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	logger.info("[Initializing]");
    	Robot.harvester.setTilt(0);
    	currPos = potAverage;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(currPos < tgtPos / 2) {
    		ThV += maxAcc;
    	} else if(currPos < tgtPos) {
    		ThV -= maxAcc;
    	} else {
    		ThV = 0;
    	}
    	
    	if(ThV < vBound) {
    		cmdSpeed = ThV;
    	} else {
    		cmdSpeed = vBound;
    	}
    	
    	if(currPos > tgtPos) {
    		Robot.harvester.setTilt(-cmdSpeed);
    	} else if(currPos < tgtPos) {
    		Robot.harvester.setTilt(cmdSpeed);
    	} else {
    		Robot.harvester.setTilt(0);
    	}
    	
    	currPos = potAverage;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(currPos == tgtPos || (Robot.harvester.getLeftLimit() &&  Robot.harvester.getRightLimit())) {
        	return true;
        } else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    	logger.info("[Ending]");
    	Robot.harvester.setTilt(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	logger.info("[Interrupted]");
    	end();
    }
}
