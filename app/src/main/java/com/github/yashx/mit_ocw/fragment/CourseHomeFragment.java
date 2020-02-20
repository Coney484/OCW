package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CourseHomeFragment extends Fragment {

    private Context c;

    public static CourseHomeFragment newInstance(Document doc) {
        Bundle args = new Bundle();
        //storing document instead of url to not load page twice
        args.putString("html", doc.html());
        CourseHomeFragment fragment = new CourseHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coursehome, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.c = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout linearLayout = view.findViewById(R.id.LLfragCourseHome);
        Element eT;
        Elements eTs;
        String html = getArguments().getString("html");
        Document doc = Jsoup.parse(html);

        //getting related topics and setting up chips
        eTs = doc.select("#related > div > ul > li");
        if (eTs != null) {
            ChipGroup cp = new ChipGroup(c);
            cp.setPadding(getDps(16f), getDps(8f), getDps(16f), getDps(0f));
            for (Element e : eTs) {
                //getting absolute url (absUrl doesn't work as doc is loaded from html)
                String url = "https://ocw.mit.edu/" + e.selectFirst("a").attr("href");
                System.out.println(url);
                String s = e.text().trim();
                if (s.contains(">")) {
                    s = s.substring(s.lastIndexOf(">") + 1);
                }
                Chip chip = new Chip(c);
                chip.setText(s);
                chip.setTag(url);
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag()));
                        startActivity(i);
                    }
                });
                cp.addView(chip);
            }
            linearLayout.addView(cp);
        }

        //Getting Description
        eT = doc.selectFirst("#description > div > p");
        if (eT != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, "Description"));
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, eT.text().trim()));
        }

        //Getting Instructor
        eTs = doc.select("p.ins");
        if (eTs != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, eTs.size() != 1 ? "Instructors" : "Instructor"));
            StringBuilder sb = new StringBuilder();
            for (Element e : eTs) {
                sb.append(e.text().trim()).append("\n");
            }
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, sb.toString().trim()));
        }

        //Getting Taught in
        eT = doc.selectFirst("p[itemprop=\"startDate\"]");
        if (eT != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, "As Taught In"));
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, eT.text().trim()));
        }

        //Getting Level
        eT = doc.selectFirst("p[itemprop=\"typicalAgeRange\"]");
        if (eT != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, "Level"));
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, eT.text().trim()));
        }
    }

    //pixel to dp
    int getDps(float f) {
        float s = getContext().getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}