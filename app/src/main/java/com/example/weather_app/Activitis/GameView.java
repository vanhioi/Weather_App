package com.example.weather_app.Activitis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.weather_app.R;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private SurfaceHolder holder;
    private boolean running;
    private Paint paint;

    private int screenWidth, screenHeight;
    private float ballX, ballY, ballRadius;
    private float ballSpeedX, ballSpeedY;
    private float paddleX, paddleY, paddleWidth, paddleHeight;
    private float paddleSpeed;

    private Bitmap ballBitmap;
    private Bitmap paddleBitmap;
    private Bitmap background;

    private int score; // Biến lưu điểm số
    private Paint scorePaint; // Paint để vẽ điểm số

    private Context context; // Context để sử dụng Intent

    //private MediaPlayer backgroundMusic;
    private MediaPlayer collisionSound;
    private MediaPlayer gameOverSound;

    public GameView(Context context) {
        super(context);
        this.context = context; // Lưu context
        holder = getHolder();
        paint = new Paint();

        scorePaint = new Paint(); // Khởi tạo Paint cho điểm số
        scorePaint.setColor(android.graphics.Color.WHITE); // Màu chữ
        scorePaint.setTextSize(100); // Kích thước chữ

        // Khởi tạo điểm số
        score = 0;

        // Khởi tạo vị trí và kích thước của quả bóng
        ballRadius = 20;
        ballX = ballRadius + 50;
        ballY = ballRadius + 50;
        ballSpeedX = 70;
        ballSpeedY = 70;

        // Khởi tạo vị trí và kích thước của paddle
        paddleWidth = 300;
        paddleHeight = 20;
        paddleX = 100;
        paddleY = 1500;
        paddleSpeed = 20;

        // Tải các hình ảnh
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clearsky);
        paddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scatteredclouds);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.nang);

        // Khởi tạo MediaPlayer cho nhạc nền và các hiệu ứng âm thanh
        //backgroundMusic = MediaPlayer.create(context, R.raw.rainy);
        //backgroundMusic.setLooping(true); // Lặp lại nhạc nền
        collisionSound = MediaPlayer.create(context, R.raw.point);
        gameOverSound = MediaPlayer.create(context, R.raw.gameover);
    }

    @Override
    public void run() {
        while (running) {

            // Bắt đầu phát nhạc nền
            //backgroundMusic.start();

            if (!holder.getSurface().isValid())
                continue;

            Canvas canvas = holder.lockCanvas();
            screenWidth = canvas.getWidth();
            screenHeight = canvas.getHeight();

            // Kéo dài hình nền để phủ toàn màn hình
            Bitmap scaledBackground = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);

            // Vẽ hình nền
            canvas.drawBitmap(scaledBackground, 0, 0, paint);

            // Vẽ quả bóng
            canvas.drawBitmap(ballBitmap, ballX - ballRadius, ballY - ballRadius, paint);

            // Vẽ paddle
            canvas.drawBitmap(paddleBitmap, paddleX, paddleY, paint);

            // Vẽ điểm số
            canvas.drawText("Score: " + score, 50, 100, scorePaint);

            // Cập nhật vị trí quả bóng
            ballX += ballSpeedX;
            ballY += ballSpeedY;

            // Kiểm tra va chạm với tường
            if ((ballX - ballRadius < 0) || (ballX + ballRadius > screenWidth)) {
                ballSpeedX = -ballSpeedX;
            }
            if (ballY - ballRadius < 0) {
                ballSpeedY = -ballSpeedY;
            }

            // Kiểm tra va chạm với paddle
            if (ballY + ballRadius > paddleY && ballX > paddleX && ballX < paddleX + paddleWidth) {
                ballSpeedY = -ballSpeedY;
                score++; // Tăng điểm số khi quả bóng chạm vào paddle
                collisionSound.start(); // Phát âm thanh va chạm
            }

            // Kiểm tra game over
            if (ballY + ballRadius > 3000) { // Kết thúc trò chơi khi quả bóng vượt quá Y = 3000
                // Dừng game
                running = false;

                // Dừng nhạc nền và phát âm thanh game over
                //backgroundMusic.stop();
                gameOverSound.start();

                // Quay lại MenuGame
                Intent intent = new Intent(context, MenuGame.class);
                context.startActivity(intent);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                paddleX = event.getX() - paddleWidth / 2;
                break;
        }
        return true;
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
