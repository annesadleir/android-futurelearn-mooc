package uk.ac.reading.sis05kol.mooc;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by rjr on 12/06/2015.
 */
public class GameBall {

    private Bitmap bitmap = null;

    private static Bitmap smallWhiteBall;

    private float xPosition = 0;

    private float yPosition = 0;

    private float xSpeed = 0;

    private float ySpeed = 0;

    private boolean inPlay = true;

    public static GameBall createSmallWhiteBall(int mCanvasWidth, int mCanvasHeight) {
        GameBall movingBall = new GameBall();
        movingBall.setxPosition((float) Math.random() * mCanvasWidth);
        movingBall.setyPosition((float) Math.random() * mCanvasHeight);
        movingBall.setxSpeed(0.4f * mCanvasWidth);
        movingBall.setySpeed(0.4f * mCanvasHeight);
        float ySpeed = movingBall.getySpeed();
        if (ySpeed > 0) {
            movingBall.setySpeed(-ySpeed);
        }
        movingBall.setBitmap(smallWhiteBall);
        return movingBall;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, xPosition - getRadius(), yPosition - getRadius(), null);
    }

    public void moveTowards(float x, float y) {
        xSpeed = (x - xPosition);
        ySpeed = (y - yPosition);
    }

    public void moveThrough(float xDirection, float yDirection) {
        xSpeed = xSpeed + 40f * xDirection;
        ySpeed = ySpeed - 40f * yDirection;
    }

    public void updatePosition(float secondsElapsed) {
        xPosition = xPosition + secondsElapsed * xSpeed;
        yPosition = yPosition + secondsElapsed * ySpeed;

    }

    public void bounceOnEdges(float mCanvasHeight, float mCanvasWidth) {
        bounceOnWidthEdges(mCanvasWidth);
        bounceOnHeightEdges(mCanvasHeight);
    }


    public void bounceOnWidthEdges(float mCanvasWidth) {
        float radius = getRadius();
        if ((xSpeed > 0 && xPosition >= mCanvasWidth - radius)
                || (xSpeed < 0 && xPosition <= radius)) {
            xSpeed = -xSpeed;
        }
    }

    public void bounceOnHeightEdges(float mCanvasHeight) {
        float radius = getRadius();
        if ((ySpeed > 0 && yPosition >= mCanvasHeight - radius)
                || (ySpeed < 0 && yPosition <= radius)) {
            ySpeed = -ySpeed;
        }
    }

    public void bounceOnTopEdge() {
        if (ySpeed < 0 && yPosition <= getRadius()) {
            ySpeed = -ySpeed;
        }
    }

    public float getRadius() {
        if (bitmap == null) {
            return 0;
        } else {
            return bitmap.getHeight() / 2;
        }
    }

    public void moveTowardsX(float x) {
        moveTowards(x, yPosition);
    }

    public void moveThroughX(float xDirection) {
        moveThrough(xDirection, 0);
    }


    public boolean inCollisionWith(GameBall movingBall) {
        float xDistanceBetweenBalls = xPosition - movingBall.getxPosition();
        float yDistanceBetweenBalls = yPosition - movingBall.getyPosition();
        float squaredDistance = xDistanceBetweenBalls * xDistanceBetweenBalls
                + yDistanceBetweenBalls * yDistanceBetweenBalls;
        float sumOfDiameters = getRadius() + movingBall.getRadius();

        return sumOfDiameters * sumOfDiameters >= squaredDistance;
    }


    public void bounceOffStationaryBall(GameBall ball) {
        //Get the present speed (this should also be the speed going away after the collision)
        float preBounceSpeed = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);

        //Change the direction of the ball
        xSpeed = xPosition - ball.getxPosition();
        ySpeed = yPosition - ball.getyPosition();

        //Get the speed after the collision
        float postBounceSpeed = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);

        //using the fraction between the original speed and present speed to calculate the needed
        //velocities in X and Y to get the original speed but with the new angle.
        xSpeed = xSpeed * preBounceSpeed / postBounceSpeed;
        ySpeed = ySpeed * preBounceSpeed / postBounceSpeed;


    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public void intoPlay() {
        this.inPlay = true;
    }

    public void outOfPlay() {
        this.inPlay = false;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    public static Bitmap getSmallWhiteBall() {
        return smallWhiteBall;
    }

    public static void setSmallWhiteBall(Bitmap smallWhiteBall) {
        GameBall.smallWhiteBall = smallWhiteBall;
    }
}
