package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Set;

import javax.swing.SwingConstants;

public class myPongModel implements PongModel {

	Ball PongBall;
	Bar LeftBar;
	Bar RightBar;
	int BarHeight;
	int PointsL;
	int PointsR;
	int BarSpeed;
	String ScoreL;
	String ScoreR;
	String message;
	String LeftPlayer;
	String RightPlayer;
	Dimension FieldSize;
	
public myPongModel(){

}

public myPongModel(String leftPlayer, String rightPlayer){
	LeftPlayer = leftPlayer;
	RightPlayer = rightPlayer;
	PointsL = 0;
	PointsR = 0;
	ScoreL = "0";
	ScoreR = "0";
	message = null;
	FieldSize = new Dimension(2000,2000);
	BarSpeed = 40;
	BarHeight = 400;
	PongBall = new Ball(new Point(FieldSize.width/2,FieldSize.height/2),new Point(10,20),30,30);
	LeftBar = new Bar(FieldSize.height/2, BarHeight, BarSpeed);
	RightBar = new Bar(FieldSize.height/2, BarHeight, BarSpeed);
	}

@Override
public void compute(Set<Input> input, long delta_t) throws InterruptedException {
	PongBall.InitSpeed = (int) (30 * delta_t/33);
	BarSpeed = (int) (40 * delta_t/33);
	if(PointsR == 10){			// Do this if the right player has 10 points.
		Thread.sleep(5000);		// Wait 5 seconds.
		message = null;			// Reset the message and the scores.
		PointsR = 0;
		PointsL = 0;
		ScoreL = "0";
		ScoreR = "0";
	}
	if(PointsL == 10){			// Do this if the left player has 10 points.
		Thread.sleep(5000);		// Wait 5 seconds.
		message = null;			// Reset the message and the scores.
		PointsR = 0;
		PointsL = 0;
		ScoreL = "0";
		ScoreR = "0";
	}
	for(Input inputValue: input){ // For each element of type Input in the hashset input.
		switch(inputValue.key){
		case LEFT:					// Do this if the input is one of the left players inputs.
			switch(inputValue.dir){
			case UP:				// Do this if the input is the up button.
				if(LeftBar.Position-LeftBar.Height/2 > 0){LeftBar.Position -= BarSpeed;}
				continue;
			case DOWN:               // Do this if the input is the down button.
				if(LeftBar.Position+LeftBar.Height/2 < FieldSize.height){LeftBar.Position += BarSpeed;}
				continue;
			}
			continue;
		case RIGHT:                 // Do this if the input is one of the right players inputs.
			switch(inputValue.dir){
			case UP:				// Do this if the input is the up button.
				if(RightBar.Position-RightBar.Height/2 > 0){RightBar.Position -= BarSpeed;}
				continue;
			case DOWN:				// Do this if the input is the down button.
				if(RightBar.Position+RightBar.Height/2 < FieldSize.height){RightBar.Position += BarSpeed;}
				continue;
			}
			continue;	
		}
	}
	if(PongBall.Position.y+10 + PongBall.Velocity.y > FieldSize.height | PongBall.Position.y + PongBall.Velocity.y < 10){	//Invert the balls Y-direction if the ball hits the bottom or top of the field.
		PongBall.Velocity.y *= -1;
	}

	
	if(PongBall.Position.x + PongBall.Velocity.x > FieldSize.width-20){		// Do this if the balls X-position in the next frame would be outside the field on the right side.
		int ymax = RightBar.Position+RightBar.Height/2;				// The top of the paddle.
		int ymin = RightBar.Position-RightBar.Height/2;				// The bottom of the paddle.
		if(PongBall.Position.y-2 <= ymax & PongBall.Position.y+2 >= ymin){	// Do this if the ball collides with the paddle.
			double distanceFromMiddle = PongBall.Position.y+10-RightBar.Position;	// The distance from the balls position to the middle of the paddle.
			double newX;
			double newY;
			distanceFromMiddle /= (RightBar.Height/2);		// distanceFromMiddle as percentage. -1.0 = at the very top of the paddle, 1.0 = bottom.
			if(distanceFromMiddle>0){					// Do this if the ball hits the lower part of the paddle.	// distanceFromMiddle as percentage. 1.0 = at the very bottom of the paddle.
				if(distanceFromMiddle > 0.75){distanceFromMiddle = 0.75;}	// Caps the distance to 0.75.
			}
			else{										// Do this if the ball hits the middle of the paddle or the top part.
				if(distanceFromMiddle < -0.75){distanceFromMiddle = -0.75;}	// Caps the distance to -0.75
			}		
			newX = Math.cos(distanceFromMiddle)*PongBall.Speed;	// New X-velocity as cosine of distanceFromMiddle, multiplied by the speed.
			newY = Math.sin(distanceFromMiddle)*PongBall.Speed;	// New Y-velocity as sine of distanceFromMiddle, multiplied by the speed.
			PongBall.Speed += PongBall.InitSpeed*0.1;									// Increase the balls speed.
			PongBall.Velocity = new Point((int)newX*-1,(int)newY);	// Sets the balls direction to newX*-1, newY.
		}
		else{
			PointsL += 1;									// Give a point to the other player.
			ScoreL = ""+PointsL;
			PongBall.Position = new Point(FieldSize.width/2,FieldSize.height/2);	// Reset the balls position.
			PongBall.Velocity = new Point(-10,0);									// Reset the balls direction toward the other player.
			RightBar.Height -= 20;											// Reduce the bar height.
			PongBall.Speed = PongBall.InitSpeed;												// Reset the balls speed.
		}
	}
	if(PongBall.Position.x + PongBall.Velocity.x < 20){					// Do this if the balls X-position in the next frame would be outside the field on the left side.
		int ymax = LeftBar.Position+LeftBar.Height/2;				// The top of the paddle.
		int ymin = LeftBar.Position-LeftBar.Height/2;				// The bottom of the paddle.
		if(PongBall.Position.y-2 <= ymax & PongBall.Position.y+2 >= ymin){	// Do this if the ball collides with the paddle.
			double distanceFromMiddle = PongBall.Position.y+10-LeftBar.Position;	// distanceFromMiddle as percentage. -1.0 = at the very top of the paddle, 1.0 = bottom.
			double newX;
			double newY;
			if(distanceFromMiddle>0){					// distanceFromMiddle as percentage. -1.0 = at the very top of the paddle, 1.0 = bottom.
				distanceFromMiddle /= (LeftBar.Height/2);
				if(distanceFromMiddle > 0.75){distanceFromMiddle = 0.75;}
			}
			else{
				distanceFromMiddle /= (LeftBar.Height/2);
				if(distanceFromMiddle < -0.75){distanceFromMiddle = -0.75;}
			}		
			newX = Math.cos(distanceFromMiddle)*PongBall.Speed;
			newY = Math.sin(distanceFromMiddle)*PongBall.Speed;
			PongBall.Speed += PongBall.InitSpeed*0.1;
			PongBall.Velocity = new Point((int)newX,(int)newY);
		}
		else{
			PointsR += 1;
			ScoreR = ""+PointsR;
			PongBall.Position = new Point(FieldSize.width/2,FieldSize.height/2);
			PongBall.Velocity = new Point(10,0);
			LeftBar.Height -= 20;
			PongBall.Speed = PongBall.InitSpeed;
		}
	}
	if(PointsR == 10){
		message = RightPlayer + " has won the match! Rematch in 5 seconds.";
	}
	if(PointsL == 10){
		message = LeftPlayer + " has won the match! Rematch in 5 seconds.";
	}
	PongBall.Position = new Point (PongBall.Velocity.x + PongBall.Position.x, PongBall.Velocity.y + PongBall.Position.y); // Increase the balls position by the balls direction.
}

@Override
public int getBarPos(BarKey k) {
	switch (k) {
    case LEFT:
        return LeftBar.Position;
    case RIGHT:
        return RightBar.Position;
    default:
    	return 0;	
	}
}

@Override
public int getBarHeight(BarKey k) {
	switch (k) {
    case LEFT:
        return LeftBar.Height;
    case RIGHT:
        return RightBar.Height;
    default:
    	return 0;	
	}
}

@Override
public Point getBallPos() {
	return PongBall.Position;
}

@Override
public String getMessage() {
	return message;
}

@Override
public String getScore(BarKey k) {
	switch (k) {
    case LEFT:
        return ScoreL;
    case RIGHT:
        return ScoreR;
    default:
    	return "";	
	}
}

@Override
public Dimension getFieldSize() {
	return FieldSize;
}	
}