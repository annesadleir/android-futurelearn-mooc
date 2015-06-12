package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TheGame extends GameThread {

    public static final int NUMBER_OF_BALLS = 2;

    private int ballsInPlay = NUMBER_OF_BALLS;

    private Set<GameBall> movingBalls = new HashSet<>();

    private GameBall paddle = new GameBall();

    private GameBall target = new GameBall();


    //This is run before anything else, so we can prepare things here
    public TheGame(GameView gameView) {
        //House keeping
        super(gameView);

        GameBall.setSmallWhiteBall(BitmapFactory.decodeResource(gameView.getContext().getResources(),
                R.drawable.small_white_ball));

        paddle.setBitmap(BitmapFactory.decodeResource(gameView.getContext().getResources(),
                R.drawable.yellow_ball));

        target.setBitmap(BitmapFactory.decodeResource(gameView.getContext().getResources(),
                R.drawable.smiley_ball));
    }

    //This is run before a new game (also after an old game)
    @Override
    public void setupBeginning() {

        ballsInPlay = NUMBER_OF_BALLS;
        movingBalls = new HashSet<>();
        for (int counter = 0; counter < NUMBER_OF_BALLS; counter++) {
            GameBall movingBall = GameBall.createSmallWhiteBall(mCanvasWidth, mCanvasHeight);
            movingBalls.add(movingBall);
        }

        paddle.setxPosition(mCanvasWidth / 2 - paddle.getRadius());
        paddle.setyPosition(mCanvasHeight);

        target.setxPosition(mCanvasWidth / 2);
        target.setyPosition(target.getRadius());
    }

    @Override
    protected void doDraw(Canvas canvas) {
        //If there isn't a canvas to draw on do nothing
        //It is ok not understanding what is happening here
        if (canvas == null) return;

        super.doDraw(canvas);

        //draw the image of the ball using the X and Y of the ball
        //drawBitmap uses top left corner as reference, we use middle of picture
        //null means that we will use the image without any extra features (called Paint)
        for (GameBall movingBall : movingBalls) {
            movingBall.draw(canvas);
        }
        paddle.draw(canvas);
        target.draw(canvas);

    }

    //This is run whenever the phone is touched by the user
    @Override
    protected void actionOnTouch(float x, float y) {
        //Increase/decrease the speed of the ball making the ball move towards the touch
        paddle.moveTowardsX(x);
    }


    //This is run whenever the phone moves around its axises
    @Override
    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
        /*
        Increase/decrease the speed of the ball.
		If the ball moves too fast try and decrease 70f
		If the ball moves too slow try and increase 70f
		 */

        paddle.moveThroughX(xDirection);

    }


    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {

        paddle.updatePosition(secondsElapsed);
        paddle.bounceOnWidthEdges(mCanvasWidth);

        List<GameBall> newGameBalls = new ArrayList<>();

        //Move the ball's X and Y using the speed (pixel/sec)
        for (GameBall movingBall : movingBalls) {
            if (movingBall.isInPlay()) {
                movingBall.updatePosition(secondsElapsed);
                movingBall.bounceOnWidthEdges(mCanvasWidth);
                movingBall.bounceOnTopEdge();

                if (paddle.inCollisionWith(movingBall)) {
                    updateScore(ballsInPlay);
                    movingBall.bounceOffStationaryBall(paddle);
                }

                if (target.inCollisionWith(movingBall)) {
                    movingBall.bounceOffStationaryBall(target);
                    newGameBalls.add(GameBall.createSmallWhiteBall(mCanvasWidth, mCanvasHeight));
                    ballsInPlay++;
                }

                if (movingBall.getyPosition() - movingBall.getRadius() >= mCanvasHeight) {
                    movingBall.setInPlay(false);
                    ballsInPlay--;
                }
            }
        }

        if (!newGameBalls.isEmpty()) {
            movingBalls.addAll(newGameBalls);
        }

        if (ballsInPlay == 0) {
            setState(STATE_LOSE);
        }


    }
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
