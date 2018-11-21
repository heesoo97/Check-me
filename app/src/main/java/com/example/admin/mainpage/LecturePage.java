package com.example.admin.mainpage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LecturePage extends Activity {
    HttpClient httpclient,httpclient2,httpclient3,httpclient4;
    HttpPost httppost,httppost2,httppost3,httppost4;
    HttpResponse response,response2,response3,response4;
    List<NameValuePair> nameValuePairs,nameValuePairs2,nameValuePairs3,nameValuePairs4;
    private TextView mtextView_lecture_name;
    TextView tv_date, tv_percent, tv_result,tv_1,tv_2,tv_3,tv_4,tv_5,tv_6,tv_7;
    String result[],result2[],result3[],result4[];
    String attendance[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        mtextView_lecture_name = (TextView)findViewById(R.id.textView_lecture_name);
        Intent intent3= getIntent();
        String it2[] = intent3.getStringArrayExtra("it2");
        String s_id = it2[0];
        String lecture_name = it2[1];

        //현재 날짜 보내기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        //Toast.makeText(LecturePage.this, lecture_name, Toast.LENGTH_SHORT).show();
        //Toast.makeText(LecturePage.this, getTime, Toast.LENGTH_SHORT).show();
        //Toast.makeText(LecturePage.this, s_id, Toast.LENGTH_SHORT).show();

        // 선택한 과목에 대한 정보를 가지고 오기 위한 통신
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://14.63.172.130/lecture.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("subject_name", lecture_name));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            result = response.split("<BR>");
            //lecture.php와 통신하여 받는 result에서 result[0]는 subject_name, [1]은 start_time, [2]는 end_time, [3]은 subject_date
            //[4]는 subject_class num ,[5]는 prof, [6]는 수업 날짜 일수

            httpclient2 = new DefaultHttpClient();
            httppost2 = new HttpPost("http://14.63.172.130/tag.php"); //tag.php가 날짜와 s_id에 따라 tagging되어 있는 횟수를 count해서 echo로 넘겨줌
            nameValuePairs2 = new ArrayList<NameValuePair>(3);
            nameValuePairs2.add(new BasicNameValuePair("subject_name", lecture_name));
            nameValuePairs2.add(new BasicNameValuePair("date", getTime));
            nameValuePairs2.add(new BasicNameValuePair("std_id", s_id));
            httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
            response2 = httpclient2.execute(httppost2);
            ResponseHandler<String> responseHandler2 = new BasicResponseHandler();
            final String response2 = httpclient2.execute(httppost2, responseHandler2);
            result2 = response2.split("<BR>");

            httpclient4 = new DefaultHttpClient();
            httppost4 = new HttpPost("http://14.63.172.130/tag_test.php");
            nameValuePairs4 = new ArrayList<NameValuePair>(2);
            nameValuePairs4.add(new BasicNameValuePair("subject_name", lecture_name));
            nameValuePairs4.add(new BasicNameValuePair("std_id", s_id));
            httppost4.setEntity(new UrlEncodedFormEntity(nameValuePairs4));
            response4 = httpclient4.execute(httppost4);
            ResponseHandler<String> responseHandler4 = new BasicResponseHandler();
            final String response4 = httpclient4.execute(httppost4, responseHandler4);
            result4 = response4.split("<BR>");

            //Toast.makeText(LecturePage.this, result4[0], Toast.LENGTH_SHORT).show();

            //Toast.makeText(LecturePage.this, result2[0], Toast.LENGTH_SHORT).show();
            //Toast.makeText(LecturePage.this, result2[1], Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }

        //해당 학생(s_id)에 대한 tagging 정보를 위한 통신


        try {
            httpclient2 = new DefaultHttpClient();
            httppost2 = new HttpPost("http://14.63.172.130/tag.php");
            nameValuePairs2 = new ArrayList<NameValuePair>(3);
            nameValuePairs2.add(new BasicNameValuePair("subject_name", lecture_name));
            nameValuePairs2.add(new BasicNameValuePair("date", getTime));
            nameValuePairs2.add(new BasicNameValuePair("std_id", s_id));
            httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
            response2 = httpclient2.execute(httppost2);
            ResponseHandler<String> responseHandler2 = new BasicResponseHandler();
            final String response2 = httpclient.execute(httppost, responseHandler2);

            //tag.php에서는 result2로 tagging된 횟수를 주어야 함. result[0]은 tagging된 횟수가 됨 //질의의 결과가 날짜와 tagging된 횟수의 count로
        } catch (Exception e) {

        }

        /*
        attendance = new String[Integer.parseInt(result[6])];
        //받아온 tagging 횟수에 따라 출석 결과 변경
        for(int i = 0; i < Integer.parseInt(result[6]); i++) {
            int tag = Integer.parseInt(result2[0]);

            if (tag >= 4) {
                attendance[i] = "출석";
            } else if (tag >= 1 && tag <= 3) {
                attendance[i] = "지각";
            } else {
                attendance[i] = "결석";
            }

            if (tag == 4) {
                attendance[i] = "출석";
            }
            else {
                attendance[i] = "결석";
            }
        }
        */

        //Toast.makeText(LecturePage.this, result2[1], Toast.LENGTH_SHORT).show();

        try {
            httpclient3 = new DefaultHttpClient();
            httppost3 = new HttpPost("http://14.63.172.130/attendance.php"); //attendance.php에서 attendance_result 값을 가져옴
            nameValuePairs3 = new ArrayList<NameValuePair>(3);
            nameValuePairs3.add(new BasicNameValuePair("subject_name", lecture_name));
            nameValuePairs3.add(new BasicNameValuePair("date", getTime));
            nameValuePairs3.add(new BasicNameValuePair("std_id", s_id));
            httppost3.setEntity(new UrlEncodedFormEntity(nameValuePairs3));
            response3 = httpclient3.execute(httppost3);
            ResponseHandler<String> responseHandler3 = new BasicResponseHandler();
            final String response3 = httpclient3.execute(httppost3, responseHandler3);
            result3 = response3.split("<BR>");
        } catch (Exception e) {
        }

        mtextView_lecture_name.setText(result[0] + "\n" + result[1] + " - " + result[2] + "\n"
                +result[5]+"교수님"+"    "+result[4]);
        mtextView_lecture_name.setTextSize(20);
        mtextView_lecture_name.setBackgroundColor(Color.rgb(248,255,255));
        mtextView_lecture_name.setGravity(Gravity.CENTER);

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
        final TableRow tableRow2 = new TableRow(this);
        final TableRow tableRow3 = new TableRow(this);
        tv_date = new TextView(this);
        tv_percent = new TextView(this);
        tv_result = new TextView(this);
        tv_1 = new TextView(this);
        tv_2 = new TextView(this);
        tv_3 = new TextView(this);
        tv_4 = new TextView(this);
        tv_5 = new TextView(this);
        tv_6 = new TextView(this);
        tv_7 = new TextView(this);

        tv_date.setText("강의일");
        tv_date.setBackgroundColor(Color.rgb(176, 208, 214));
        tv_date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tv_1.setText(""); //text3은 칸 구별을 위한 검은 선
        tv_1.setWidth(5);
        tv_1.setBackgroundColor(Color.BLACK);

        tv_percent.setText("\t\t" + "태깅 퍼센트" + "\t\t");  //text2는 tagging 퍼센트 //111 대신에 tagging 한 퍼센트가 들어가야 함 //php로 태깅을 받아서 결정
        tv_percent.setBackgroundColor(Color.rgb(176, 208, 214));
        tv_percent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tv_2.setText("");
        tv_2.setWidth(5);
        tv_2.setBackgroundColor(Color.BLACK);

        tv_result.setText("\t\t" + "출석 결과" + "\t\t");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
        tv_result.setBackgroundColor(Color.rgb(176, 208, 214));
        tv_result.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tv_3.setText("");
        tv_3.setHeight(5);
        tv_3.setBackgroundColor(Color.BLACK);
        tv_4.setText("");
        tv_4.setHeight(5);
        tv_4.setBackgroundColor(Color.BLACK);
        tv_5.setText("");
        tv_5.setHeight(5);
        tv_5.setBackgroundColor(Color.BLACK);
        tv_6.setText("");
        tv_6.setHeight(5);
        tv_6.setBackgroundColor(Color.BLACK);
        tv_7.setText("");
        tv_7.setHeight(5);
        tv_7.setBackgroundColor(Color.BLACK);



        tv_date.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_percent.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_2.setGravity(Gravity.CENTER_HORIZONTAL);
        tv_result.setGravity(Gravity.CENTER_HORIZONTAL);


        //표 상단에 표현될 목록 부분을 위한 행:tableRow2
        tableRow2.addView(tv_date);
        tableRow2.addView(tv_1);
        tableRow2.addView(tv_percent);
        tableRow2.addView(tv_2);
        tableRow2.addView(tv_result);

        //표에서 목록과 내용을 분리하기 위한 행:tableRow3
        tableRow3.addView(tv_3);
        tableRow3.addView(tv_4);
        tableRow3.addView(tv_5);
        tableRow3.addView(tv_6);
        tableRow3.addView(tv_7);

        tv_date.setTextSize(17);
        tv_1.setTextSize(17);
        tv_percent.setTextSize(17);
        tv_2.setTextSize(17);
        tv_result.setTextSize(17);

        tableRow2.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow3.setGravity(Gravity.CENTER_HORIZONTAL);
        tableLayout.addView(tableRow2);
        tableLayout.addView(tableRow3);


        //출석이 표현 될 표
        for (int i = 0; i < Integer.parseInt(result[6]) * 2; i++) {
            final TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            final TextView text = new TextView(this); //text는 수업 강의일
            final TextView text2 = new TextView(this); //출석 tagging 퍼센트
            final TextView text3 = new TextView(this); //칸 구별을 위한 검은 선
            final TextView text4 = new TextView(this); // 칸 구별을 위한 검은 선
            final TextView text5 = new TextView(this); // 출석 결과
            if(i % 2 == 0)
                {
                    text.setText("\t\t" + result[(i / 2) + 7] + "\t\t"); //수업 강의일
                    text.setBackgroundColor(Color.rgb(176, 208, 214));
                    text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    text3.setText(""); //text3은 칸 구별을 위한 검은 선
                    text3.setWidth(5);
                    text3.setBackgroundColor(Color.BLACK);

                    text2.setText("tagging 한 퍼센트_출석 퍼센트");  //text2는 tagging 퍼센트 //111 대신에 tagging 한 퍼센트가 들어가야 함 //php로 태깅을 받아서 결정
                    text2.setBackgroundColor(Color.rgb(176, 208, 214));
                    text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    if (result4[i / 2].equals("a")) {
                        text2.setText("");
                        text2.setBackgroundColor(Color.rgb(176, 208, 214));
                        text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }

                    else {
                        int percent = Integer.parseInt(result4[i / 2]) * 20;

                        //text2.setText(result4[i / 2]);
                        text2.setText("\t\t" + String.valueOf(percent) + "%" + "\t\t");
                        text2.setBackgroundColor(Color.rgb(176, 208, 214));
                        text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }

                    //int percent = Integer.parseInt(result4[i/2]) * 20;

                    //text2.setText(String.valueOf(percent) + "%");  //text2는 tagging 퍼센트 //111 대신에 tagging 한 퍼센트가 들어가야 함 //php로 태깅을 받아서 결정
                    /*text2.setText(result4[(i+1) / 2 + 1]);
                    text2.setBackgroundColor(Color.rgb(176, 208, 214));
                    text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));*/

                    /*
                    if (result4[index].equals("")) {
                        text2.setText("");
                        text2.setBackgroundColor(Color.rgb(176, 208, 214));
                        text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        index++;
                    }
                    else {
                        text2.setText(result4[index++]);
                        text2.setBackgroundColor(Color.rgb(176, 208, 214));
                        text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    */

                    /*
                    int percent = Integer.parseInt(result2[i/2]);
                    text2.setText("\t\t"+percent*20+"%\t\t");  //text2는 tagging 퍼센트 //111 대신에 tagging 한 퍼센트가 들어가야 함 //php로 태깅을 받아서 결정
                    text2.setBackgroundColor(Color.rgb(176, 208, 214));
                    text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));*/

                    text4.setText("");
                    text4.setWidth(5);
                    text4.setBackgroundColor(Color.BLACK);


                    /*
                    text5.setText("출석 결과");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
                    text5.setBackgroundColor(Color.rgb(176, 208, 214));
                    text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));*/

                    if(result3[i/2].equals("csa"))
                    {
                        text5.setText("\t\t"+"출석"+"\t\t");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
                        text5.setBackgroundColor(Color.rgb(176, 208, 214));
                        text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    else if(result3[i/2].equals("jka"))
                    {
                        text5.setText("\t\t"+"지각"+"\t\t");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
                        text5.setBackgroundColor(Color.rgb(176, 208, 214));
                        text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    else if(result3[i/2].equals("ksa"))
                    {
                        text5.setText("\t\t"+"결석"+"\t\t");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
                        text5.setBackgroundColor(Color.rgb(176, 208, 214));
                        text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }
                    else
                    {
                        text5.setText("\t\t"+"미정"+"\t\t");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
                        text5.setBackgroundColor(Color.rgb(176, 208, 214));
                        text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                    }


                    /*text5.setText("\t\t"+attendance[i/2]+"\t\t");   //php로 위에 퍼센트를 받아온 후 그에 따른 출석 그룹 분류
                    text5.setBackgroundColor(Color.rgb(176, 208, 214));
                    text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));*/

                }
                else{
                    if (i == Integer.parseInt(result[6]) * 2 - 1) {
                        text.setText("");
                        text.setBackgroundColor(Color.WHITE);
                        text.setHeight(5);
                        text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        text3.setText("");
                        text3.setBackgroundColor(Color.WHITE);
                        text3.setHeight(5);

                        text2.setText("");
                        text2.setBackgroundColor(Color.WHITE);
                        text2.setHeight(5);
                        text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        text4.setText("");
                        text4.setBackgroundColor(Color.WHITE);
                        text4.setHeight(5);

                        text5.setText("");
                        text5.setBackgroundColor(Color.WHITE);
                        text5.setHeight(5);
                        text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));


                    }
                    else {
                        text.setText("");
                        text.setBackgroundColor(Color.BLACK);
                        text.setHeight(5);
                        text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        text3.setText("");
                        text3.setBackgroundColor(Color.BLACK);
                        text3.setHeight(5);

                        text2.setText("");
                        text2.setBackgroundColor(Color.BLACK);
                        text2.setHeight(5);
                        text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        text4.setText("");
                        text4.setBackgroundColor(Color.BLACK);
                        text4.setHeight(5);

                        text5.setText("");
                        text5.setBackgroundColor(Color.BLACK);
                        text5.setHeight(5);
                        text5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                    }


                }

            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text2.setGravity(Gravity.CENTER_HORIZONTAL);
            text5.setGravity(Gravity.CENTER_HORIZONTAL);

            tableRow.addView(text);
            tableRow.addView(text3);
            tableRow.addView(text2);
            tableRow.addView(text4);
            tableRow.addView(text5);

            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

            text.setTextSize(17);
            text2.setTextSize(17);
            text3.setTextSize(17);
            text4.setTextSize(17);
            text5.setTextSize(17);

            tableLayout.addView(tableRow);
            tableLayout.setGravity(Gravity.CENTER);
        }
    }
}