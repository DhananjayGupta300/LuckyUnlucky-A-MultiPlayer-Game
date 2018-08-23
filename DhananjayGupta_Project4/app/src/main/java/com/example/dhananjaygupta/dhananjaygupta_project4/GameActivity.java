package com.example.dhananjaygupta.dhananjaygupta_project4;
//suporting packages
import android.annotation.SuppressLint;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Random;
import android.widget.Toast;

//the main game activity. No user interaction
public class GameActivity extends AppCompatActivity
{
//game ends on back button press
    @Override
    public void onBackPressed() {
        Gme_Over = true;
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
//variable declaration
    //variable definition is as per in the project specification
    public static final int CATASTROPHE = 4;
    public static final int JACKPOT = 0;
    public static final int P1 = 9;
    public static final int P2 = 10;
    public static final int NEAR_GRP = 2;

    public boolean Gme_Over = false;

    public static final int RANDOM_SHOT = 5;
    public static final int SAME_G_SHOT = 7;
    public static final int TARGET_H_SHOT = 8;
    public static final int CLOSE_G_SHOT = 6;
    public static final int NEAR_MISSED = 1;
    public static final int PLY_START = 11;
    public static final int GME_OVER = 12;
    public static final int BIG_MISSED = 3;

    final int count_hole = 50;

    public int p2_pos = -1;
    public int p1_pos = 1;
    public int winning_goal = 0;
//thread for player 2
    player2 player2_thread;
//thread for player 1
    player1 player1_thread;


    static String current_player = "Player1";
    ListView listview;
    ListAdapter myadapter;
    //holes are stored here
    ArrayList<Hole_Configuration> list_hole = new ArrayList<>();


    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            int wht = message.what;
// if the player is to start
            if (wht == PLY_START)
            {
                final String toast_message1 = message.arg1 == 1 ? "Player 1" : "Player 2";

                Toast.makeText(getApplicationContext(), toast_message1 + "'s turn...", Toast.LENGTH_SHORT).show();
            }
// for player 1
            else if (wht == P1)
            {

                p1_pos = message.arg1;
                list_hole.get(p1_pos).visited += 1;
                list_hole.get(p1_pos).p1 = true;
                list_hole.get(p1_pos).clashed = (p1_pos == p2_pos);
                myadapter.notifyDataSetChanged();
                listview.setSelection(p1_pos);
                listview.requestFocus();

                Handler player1_Handler = player1_thread.obtainPlayer1Handler();
                Message message1 = null;
                if (list_hole.get(p1_pos).winning_goal)
                {
                    Gme_Over = true;
                    message1 = player1_Handler.obtainMessage(GameActivity.JACKPOT);
                    Toast.makeText(getApplicationContext(), "Player1 Wins", Toast.LENGTH_LONG).show();

                }
                else if (p1_pos == p2_pos)
                {
                    Gme_Over = true;
                    message1 = player1_Handler.obtainMessage(GameActivity.CATASTROPHE);
                    Toast.makeText(getApplicationContext(), "Player1 lost due to catastrophe", Toast.LENGTH_LONG).show();

                }
                else if (list_hole.get(winning_goal).grp == list_hole.get(p1_pos).grp)
                {
                    message1 = player1_Handler.obtainMessage(GameActivity.NEAR_MISSED);

                }
                else if (list_hole.get(winning_goal).grp + 1 == list_hole.get(p1_pos).grp ||
                    list_hole.get(winning_goal).grp - 1 == list_hole.get(p1_pos).grp)
                {
                    message1 = player1_Handler.obtainMessage(GameActivity.NEAR_GRP);

                }
                else
                {
                    message1 = player1_Handler.obtainMessage(GameActivity.BIG_MISSED);

                }
                current_player = "Player2";

                player1_Handler.sendMessage(message1);

            }
            else if(wht == P2)
            {
                //list_hole.get(p2_pos).player2 = false;
                p2_pos =message.arg1;
                list_hole.get(p2_pos).visited +=2;
                list_hole.get(p2_pos).p2 =true;
                list_hole.get(p2_pos).clashed =(p1_pos ==p2_pos);
                myadapter.notifyDataSetChanged();
                listview.setSelection(p2_pos);
                listview.requestFocus();

                Handler p2_handler = player2_thread.getP2_Handler();
                Message message2 = null;
                if(list_hole.get(p2_pos).winning_goal)
                {
                    Gme_Over = true;
                    message2 = p2_handler.obtainMessage(GameActivity.JACKPOT);
                    Toast.makeText(getApplicationContext(), "Player2 Wins", Toast.LENGTH_LONG).show();

                }
                else if(p1_pos ==p2_pos )
                {
                    Gme_Over = true;
                    message2 = p2_handler.obtainMessage(GameActivity.CATASTROPHE);
                    Toast.makeText(getApplicationContext(), "Player2 lost due to catastrophe", Toast.LENGTH_LONG).show();

                }
                else if(list_hole.get(winning_goal).grp ==list_hole.get(p2_pos).grp)
                {
                    message2 = p2_handler.obtainMessage(GameActivity.NEAR_MISSED);

                }
                else if(list_hole.get(winning_goal).grp +1==list_hole.get(p2_pos).grp ||
                list_hole.get(winning_goal).grp -1==list_hole.get(p2_pos).grp)
                {
                    message2 = p2_handler.obtainMessage(GameActivity.NEAR_GRP);

                }
                else
                {
                    message2 = p2_handler.obtainMessage(GameActivity.BIG_MISSED);

                }
                current_player ="Player1";

                p2_handler.sendMessage(message2);

            }
            else if (Gme_Over){
                final String toast_message3 = message.arg2 == 1 ? "Won" : "Lost";
                final String toast_message2 = message.arg1 == 1 ? "Player 1" : "Player 2";


                Toast.makeText(getApplicationContext(),toast_message2+" " + toast_message3 + "...",Toast.LENGTH_SHORT).show();
            }
        }
    };
// This class gives the hole configuraton
    class Hole_Configuration
    {

        boolean winning_goal;
        boolean clashed;
        int grp;
        int visited;
        boolean p2;
        boolean p1;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        data_intializer();
        myadapter = new ListAdapter(getApplicationContext(),list_hole);

        listview = (ListView) findViewById(R.id.listview);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setAdapter(myadapter);

        player1_thread = new player1();
        player1_thread.start();
        current_player = "Player1";

        player2_thread = new player2();
        player2_thread.start();

    }

    private void data_intializer()
    {
        int winning_hole = new Random().nextInt(50);
        winning_goal = winning_hole;
        int x =0;
        while(x< count_hole)
        {
            Hole_Configuration hole = new Hole_Configuration();
            hole.grp = x/10;
            hole.visited = 0;
            hole.winning_goal = (x == winning_hole);
            hole.p2 = false;
            hole.p1 = false;
            hole.clashed = false;
            list_hole.add(hole);
            x++;
        }
    }

    public class player1 extends Thread
    {

        private Handler player1_Handler;
        Random random = new Random();

        @SuppressLint("HandlerLeak")
        @Override
        public void run()
        {
            Looper.prepare();

            player1_Handler = new Handler()
            {
                public void handleMessage(Message message) {

                    if (Gme_Over) {
                        getLooper().quitSafely();
                        return;
                    }
                    int wht = message.what ;
                    if (wht==JACKPOT) {
                        message = myHandler.obtainMessage(GameActivity.GME_OVER);
                        message.arg2 = 1;
                        message.arg1 = 1;
                        myHandler.sendMessage(message);
                        getLooper().quitSafely();
                        return;
                    }
                    else if( wht ==CATASTROPHE)
                    {
                        message = myHandler.obtainMessage(GameActivity.GME_OVER);
                        message.arg2 = 2;
                        message.arg1 = 1;
                        myHandler.sendMessage(message);
                        getLooper().quitSafely();
                        return;
                    }
                    else if(wht== NEAR_GRP)
                    {
                        System.out.println("Near Group Bru P1!");
                    }
                    else if(wht==NEAR_MISSED)
                    {
                        System.out.println("Near Missed Bru P1!");
                    }

                    else if(wht== BIG_MISSED)
                    {
                        System.out.println("Big missed Bru P1!");
                    }

                    while (current_player.equalsIgnoreCase("Player2"))
                    {
                        try
                        {
                            Thread.sleep(2050);
                        }
                        catch (InterruptedException e)
                        {
                            System.out.println("Thread got interrupted!") ;
                        }

                    }
                    if (Gme_Over) {
                        getLooper().quitSafely();
                        return;
                    }

                    try
                    {
                        Thread.sleep(1050);
                    }
                    catch (InterruptedException excep) {
                        System.out.println("Thread has been interrupted!") ;
                    }
                    Message message2 = myHandler.obtainMessage(GameActivity.PLY_START);
                    message2.arg1 = 1;
                    myHandler.sendMessage(message2) ;
                    try
                    {
                        Thread.sleep(1050);
                    }
                    catch (InterruptedException excep)
                    {
                        System.out.println("Thread has been interrupted!") ;
                    }

                    int shot_my =  p1_pos == -1? RANDOM_SHOT : new Random().nextInt(4) + 5;


                    if (shot_my==RANDOM_SHOT)
                    {
                        shot_my = randomShotGenerator();
                    }
                    else if(shot_my==CLOSE_G_SHOT)
                    {
                        shot_my = closeShotGenerator();
                    }
                    else if (shot_my==SAME_G_SHOT)
                    {
                        shot_my = sameShotGenerator();
                    }
                    else if (shot_my== TARGET_H_SHOT)
                    {
                        shot_my = randomShotGenerator();
                    }

                    message = myHandler.obtainMessage(GameActivity.P1);
                    message.arg1 = shot_my;
                    myHandler.sendMessage(message) ;
                }
            };

            Message message = myHandler.obtainMessage(GameActivity.PLY_START);
            message.arg1 = 1;
            myHandler.sendMessage(message) ;
            try
            {
                Thread.sleep(3050);
            }
            catch (InterruptedException excep)
            {
                System.out.println("Thread is interupted ") ;
            }
            int shot_my =  randomShotGenerator();
            message = myHandler.obtainMessage(GameActivity.P1);
            message.arg1 = shot_my;
            myHandler.sendMessage(message) ;
            Looper.loop();
        }

        public int randomShotGenerator()
        {
            int shot = random.nextInt(count_hole);
            // 1 - 1 == 0 ,3 - 1 == 2   -> Invalid
            // 0 - 1 == -1, 2 - 1 == 1  -> Valid
            while(list_hole.get(shot).visited - 1 == 0 || list_hole.get(shot).visited - 1 == 2 )
            {
                shot = random.nextInt(count_hole);
            }
            return shot;
        }

        public int limitedShotGenerator(int cur_group, int lower_group, int upper_group)
        {

            int possible_shot=0;
            int shot = random.nextInt(10);

            if (random.nextInt(3)== 0) {
                shot = (10*cur_group)+shot;
                possible_shot = 0 + (cur_group*10);
            }
            else if (random.nextInt(3)== 1)
            {
                shot = (10*lower_group)+shot;
                possible_shot = 0 + (lower_group*10);
            }
            else if (random.nextInt(3)== 2){
                    shot = (10*upper_group)+shot;
                    possible_shot = 0 + (upper_group*10);
            }
            System.out.println("");
            if(list_hole.get(shot).visited - 1 == 0 || list_hole.get(shot).visited - 1 == 2)
            {

                possible_shot = 9+possible_shot;
                shot = possible_shot;
                while(possible_shot < 10 && (list_hole.get(possible_shot).visited - 1 == 0
                        || list_hole.get(possible_shot).visited - 1 == 2) ) {
                    possible_shot++;
                }

                shot = possible_shot == 10 ? randomShotGenerator() : possible_shot;
            }

            return shot;
        }

        public int closeShotGenerator()
        {
            int cur_group = list_hole.get(p1_pos).grp;
            int upper_group = cur_group + 1 < 4 ? cur_group + 1 : 4;
            int lower_group = cur_group - 1 > -1 ? cur_group - 1 : 0;

            return limitedShotGenerator(cur_group,lower_group,upper_group);
        }

        public int sameShotGenerator()
        {
            int cur_group = list_hole.get(p1_pos).grp;
            return limitedShotGenerator(cur_group,cur_group,cur_group);
        }

        public Handler obtainPlayer1Handler() {
            return player1_Handler;
        }
    }

    public class player2 extends Thread
    {

        private Handler p2_Handler;
        Random rand = new Random();

        @SuppressLint("HandlerLeak")
        @Override
        public void run()
        {

            Looper.prepare();

            p2_Handler = new Handler() {
                public void handleMessage(Message message)
                {

                    int shot_my =  p2_pos == -1? RANDOM_SHOT : new Random().nextInt(4) + 5;

                    if(Gme_Over)
                    {
                        getLooper().quitSafely();
                        return;
                    }
                    int wht = message.what ;
                    if (wht==JACKPOT) {

                        message = myHandler.obtainMessage(GameActivity.GME_OVER);
                        message.arg2 = 1;
                        message.arg1 = 2;
                        myHandler.sendMessage(message);
                        getLooper().quitSafely();
                        return;
                    }
                    else if (wht== NEAR_MISSED)
                    {

                        shot_my = SAME_G_SHOT;
                    }
                    else if (wht==NEAR_GRP)
                    {
                        shot_my = CLOSE_G_SHOT;
                    }
                    else if (wht==BIG_MISSED)
                    {
                        shot_my = TARGET_H_SHOT;
                    }
                    else if (wht== CATASTROPHE)
                    {
                            message = myHandler.obtainMessage(GameActivity.GME_OVER);
                            message.arg2 = 2;
                            message.arg1 = 2;

                            myHandler.sendMessage(message) ;
                            getLooper().quitSafely();

                            return;

                    }


                    while(current_player.equalsIgnoreCase("Player1"))
                    {

                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException excep)
                        {
                            System.out.println("Thread interrupted!") ;
                        }

                    }

                    if (Gme_Over) {

                        getLooper().quitSafely();
                        return;
                    }
                    try
                    {
                        Thread.sleep(1050);
                    }
                    catch (InterruptedException excep)
                    {
                        System.out.println("Thread has been interrupted") ;
                    }
                    Message message2 = myHandler.obtainMessage(GameActivity.PLY_START);
                    message2.arg1 = 2;
                    myHandler.sendMessage(message2) ;
                    try
                    {
                        Thread.sleep(1050);
                    }
                    catch (InterruptedException excep)
                    {
                        System.out.println("Thread interrupted!") ;
                    }

                    if (shot_my==RANDOM_SHOT)
                    {

                        shot_my = randomShotGenerator();
                    }
                    else if (shot_my==CLOSE_G_SHOT)
                    {

                        shot_my = closeShotGenerator();
                    }
                    else if (shot_my==SAME_G_SHOT)
                    {

                        shot_my = sameShotGenerator();
                    }

                    else if (shot_my==TARGET_H_SHOT)
                    {

                            shot_my = randomShotGenerator();

                    }

                    message = myHandler.obtainMessage(GameActivity.P2);
                    message.arg1 = shot_my;
                    myHandler.sendMessage(message) ;
                }
            };
            int shot_my =  randomShotGenerator();
            Message message = myHandler.obtainMessage(GameActivity.PLY_START);
            message.arg1 = 2;
            myHandler.sendMessage(message) ;



            try
            {
                Thread.sleep(3050);
            }
            catch (InterruptedException excep)
            {
                System.out.println("Thread interrupted!") ;
            }
            message = myHandler.obtainMessage(GameActivity.P2);
            message.arg1 = shot_my;
            myHandler.sendMessage(message) ;

            Looper.loop();


        }

        public int randomShotGenerator() {
            int shot = rand.nextInt(count_hole);
            // 1 - 1 == 0 ,3 - 1 == 2   -> Invalid
            // 0 - 1 == -1, 2 - 1 == 1  -> Valid
            while(list_hole.get(shot).visited - 2 == 0 || list_hole.get(shot).visited - 2 == 1 ) {
                shot = rand.nextInt(count_hole);
            }
            return shot;
        }

        public int limitedShotGenerator(int cur_group, int lower_group, int upper_group) {
            int possible_shot=0;
            int shot = rand.nextInt(10);

            if (rand.nextInt(3)==0) {

                shot = (10*cur_group )+shot;
                possible_shot = (cur_group*10)+0;
            }
            else if (rand.nextInt(3)==1) {
                shot = (10*lower_group)+shot;
                possible_shot = (lower_group*10)+0;
            }
            else if (rand.nextInt(3)==2){
                    shot = (10*upper_group)+shot;
                    possible_shot = (upper_group*10)+0;

            }

            if(list_hole.get(shot).visited - 2 == 0 || list_hole.get(shot).visited - 2 == 1) {

                possible_shot = 9+ possible_shot;
                shot = possible_shot;
                while(possible_shot < 10 && (list_hole.get(possible_shot).visited - 2 == 0
                        || list_hole.get(possible_shot).visited - 2 == 1) ) {
                    possible_shot++;
                }
                shot = possible_shot == 10 ? randomShotGenerator() : possible_shot;

            }

            return shot;
        }

        public int closeShotGenerator() {
            int cur_group = list_hole.get(p1_pos).grp;
            int upper_group = cur_group + 1 < 4 ? cur_group + 1 : 4;
            int lower_group = cur_group - 1 > -1 ? cur_group - 1 : 0;

            return limitedShotGenerator(cur_group,lower_group,upper_group);
        }

        public int sameShotGenerator() {
            int cur_group = list_hole.get(p1_pos).grp;
            return limitedShotGenerator(cur_group,cur_group,cur_group);
        }

        public Handler getP2_Handler() {
            return p2_Handler;
        }
    }


}
