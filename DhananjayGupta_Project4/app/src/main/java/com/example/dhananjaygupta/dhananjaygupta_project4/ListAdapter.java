package com.example.dhananjaygupta.dhananjaygupta_project4;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Dhananjay Gupta on 4/12/18.
 */

public class ListAdapter extends BaseAdapter
{
    Context myContext;

    ArrayList<GameActivity.Hole_Configuration> myholes;

    public ListAdapter(Context appcontext, ArrayList<GameActivity.Hole_Configuration> holes)
    {
        myholes = holes;

        myContext = appcontext;

    }


    @Override
    public long getItemId(int x) {return 0;}

    @Override
    public Object getItem(int x) {return myholes.get(x);}

    @Override
    public int getCount() {return myholes.size();}

    @Override
    public View getView(int x, View view, ViewGroup viewgroup)
    {
        view = LayoutInflater.from(myContext).inflate(R.layout.hole_list,viewgroup,false);

        ImageView imageView = (ImageView)view.findViewById(R.id.IV2);


        if (myholes.get(x).clashed)
        {

            imageView.setBackgroundResource(android.R.drawable.ic_delete);

        }
        else if (myholes.get(x).p2)
        {

            imageView.setBackgroundResource(android.R.drawable.presence_online);

        }
        else if (myholes.get(x).p1)
        {

            imageView.setBackgroundResource(android.R.drawable.presence_invisible);

        }
        else if (myholes.get(x).winning_goal && (myholes.get(x).p2 || myholes.get(x).p1))
        {

            imageView.setBackgroundResource(android.R.drawable.btn_star_big_on);

        }
        else if (myholes.get(x).p1 && myholes.get(x).p2)
        {

            imageView.setBackgroundResource(android.R.drawable.presence_busy);

        }
        else if(myholes.get(x).winning_goal)
        {

            imageView.setBackgroundResource(android.R.drawable.btn_star_big_off);

        }
        else
        {

            imageView.setBackgroundResource(android.R.drawable.radiobutton_off_background);

        }

        return view;
    }
}
