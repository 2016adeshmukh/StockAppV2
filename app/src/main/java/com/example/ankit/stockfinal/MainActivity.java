package com.example.ankit.stockfinal;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int count;
    LineGraphSeries<DataPoint> c;
    LineGraphSeries<DataPoint> cav;
    LineGraphSeries<DataPoint> cav2;
    LineGraphSeries<DataPoint> cav3;
    LineGraphSeries<DataPoint> cav4;
    LineGraphSeries<DataPoint> vav;
    GraphView graph;

    TextView txt;

    ToggleButton tog;
    double sum;
    double avg;

    Spinner spin;
    String spinsel;

    ArrayList<String> cval=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Firebase ref=new Firebase("https://stocktest.firebaseio.com/");

        txt=(TextView)findViewById(R.id.txt);
        tog=(ToggleButton)findViewById(R.id.tog);
        graph=(GraphView)findViewById(R.id.graph);
        spin=(Spinner)findViewById(R.id.spin);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        String[] items=new String[]{"all", "5", "10", "20"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items);
        spin.setAdapter(adapter);

        c=new LineGraphSeries<DataPoint>();
        cav=new LineGraphSeries<DataPoint>();
        cav.setColor(Color.RED);
        cav2=new LineGraphSeries<DataPoint>();
        cav2.setColor(Color.RED);
        cav3=new LineGraphSeries<DataPoint>();
        cav3.setColor(Color.RED);
        cav4=new LineGraphSeries<DataPoint>();
        cav4.setColor(Color.RED);
        vav=new LineGraphSeries<DataPoint>();
        count=0;
        sum=0;
        avg=0;

        spinsel="all";
        graph.addSeries(c);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinsel=spin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tog.isChecked())
                {
                    if(spinsel.equals("all"))
                    {
                        graph.addSeries(cav);
                    }
                    if(spinsel.equals("5"))
                    {
                        graph.addSeries(cav2);
                    }
                    if(spinsel.equals("10"))
                    {
                        graph.addSeries(cav3);
                    }
                    if(spinsel.equals("20"))
                    {
                        graph.addSeries(cav4);
                    }
                }
                else
                {
                    graph.removeAllSeries();
                    graph.addSeries(c);
                }
            }
        });

        ref.child("0").child("data").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ADTS(dataSnapshot, c, cav, cav2, cav3, cav4, "c");
                count++;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        graph.removeSeries(c);
        graph.addSeries(c);

    }

    public void ADTS(DataSnapshot snapshot, LineGraphSeries<DataPoint> dataseries, LineGraphSeries<DataPoint> averageseries,LineGraphSeries<DataPoint> averageseries2,LineGraphSeries<DataPoint> averageseries3,LineGraphSeries<DataPoint> averageseries4, String datatype)
    {
        String valuec = snapshot.child(count + "").child(datatype).getValue().toString();
        if (!valuec.equals("")) {
            DataPoint dp = new DataPoint(count, Double.parseDouble(valuec));
            dataseries.appendData(dp, false, count + 10);

            sum+=Double.parseDouble(valuec);
            double tempc = (double) count;
            cval.add(valuec);
            avg = sum / (tempc + 1);
            DataPoint dpvg = new DataPoint(count, avg);
            averageseries.appendData(dpvg, false, count + 10);

            if(tempc<5)
            {
                avg = sum / (tempc + 1);
                dpvg = new DataPoint(count, avg);
                averageseries2.appendData(dpvg, false, count + 10);
                averageseries3.appendData(dpvg, false, count + 10);
                averageseries4.appendData(dpvg, false, count + 10);
            }
            else if(tempc<10)
            {
                avg = sum / (tempc + 1);
                dpvg = new DataPoint(count, avg);
                averageseries3.appendData(dpvg, false, count + 10);
                averageseries4.appendData(dpvg, false, count + 10);

                double sum2=Double.parseDouble(cval.get(count-5))+Double.parseDouble(cval.get(count-4))+Double.parseDouble(cval.get(count-3))+Double.parseDouble(cval.get(count-2))+Double.parseDouble(cval.get(count-1));
                avg=sum2/(double)5;
                dpvg = new DataPoint(count, avg);
                averageseries2.appendData(dpvg, false, count + 10);
            }
            else if(tempc<20)
            {
                avg = sum / (tempc + 1);
                dpvg = new DataPoint(count, avg);
                averageseries4.appendData(dpvg, false, count + 10);

                double sum2=Double.parseDouble(cval.get(count-5))+Double.parseDouble(cval.get(count-4))+Double.parseDouble(cval.get(count-3))+Double.parseDouble(cval.get(count-2))+Double.parseDouble(cval.get(count-1));
                avg=sum2/(double)5;
                dpvg = new DataPoint(count, avg);
                averageseries2.appendData(dpvg, false, count + 10);

                sum2=Double.parseDouble(cval.get(count-10))+Double.parseDouble(cval.get(count-9))+Double.parseDouble(cval.get(count-8))+Double.parseDouble(cval.get(count-7))+Double.parseDouble(cval.get(count-6))+Double.parseDouble(cval.get(count-5))+Double.parseDouble(cval.get(count-4))+Double.parseDouble(cval.get(count-3))+Double.parseDouble(cval.get(count-2))+Double.parseDouble(cval.get(count-1));
                avg=sum2/(double)10;
                dpvg = new DataPoint(count, avg);
                averageseries3.appendData(dpvg, false, count + 10);
            }
            else
            {
                double sum2=Double.parseDouble(cval.get(count-5))+Double.parseDouble(cval.get(count-4))+Double.parseDouble(cval.get(count-3))+Double.parseDouble(cval.get(count-2))+Double.parseDouble(cval.get(count-1));
                avg=sum2/(double)5;
                dpvg = new DataPoint(count, avg);
                averageseries2.appendData(dpvg, false, count + 10);
//
                sum2=Double.parseDouble(cval.get(count-10))+Double.parseDouble(cval.get(count-9))+Double.parseDouble(cval.get(count-8))+Double.parseDouble(cval.get(count-7))+Double.parseDouble(cval.get(count-6))+Double.parseDouble(cval.get(count-5))+Double.parseDouble(cval.get(count-4))+Double.parseDouble(cval.get(count-3))+Double.parseDouble(cval.get(count-2))+Double.parseDouble(cval.get(count-1));
                avg=sum2/(double)10;
                dpvg = new DataPoint(count, avg);
                averageseries3.appendData(dpvg, false, count + 10);
//
                sum2=Double.parseDouble(cval.get(count-20))+Double.parseDouble(cval.get(count-19))+Double.parseDouble(cval.get(count-18))+Double.parseDouble(cval.get(count-17))+Double.parseDouble(cval.get(count-16))+Double.parseDouble(cval.get(count-15))+Double.parseDouble(cval.get(count-14))+Double.parseDouble(cval.get(count-13))+Double.parseDouble(cval.get(count-12))+Double.parseDouble(cval.get(count-11))+Double.parseDouble(cval.get(count-10))+Double.parseDouble(cval.get(count-9))+Double.parseDouble(cval.get(count-8))+Double.parseDouble(cval.get(count-7))+Double.parseDouble(cval.get(count-6))+Double.parseDouble(cval.get(count-5))+Double.parseDouble(cval.get(count-4))+Double.parseDouble(cval.get(count-3))+Double.parseDouble(cval.get(count-2))+Double.parseDouble(cval.get(count-1));
                avg=sum2/(double)20;
                dpvg = new DataPoint(count, avg);
                averageseries4.appendData(dpvg, false, count + 10);
            }

        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
