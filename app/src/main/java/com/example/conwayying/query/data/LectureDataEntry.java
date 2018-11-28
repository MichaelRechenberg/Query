package com.example.conwayying.query.data;

import android.util.Pair;

import com.example.conwayying.query.data.entity.AcademicClass;
import com.example.conwayying.query.data.entity.Lecture;

/**
 * Composition of Lecture and some information relevant to displaying a Lecture
 *  in a list
 */
public class LectureDataEntry {
    // A Lecture
    public Lecture lecture;
    // First integer is number of resolved notes, second integer is number of unresolved notes
    //  for this Lecture
    public Pair<Integer, Integer> noteResolvedCountPair;
    // First integer is number of resolved confusion marks, second integer is number of unresolved confusion marks
    //  for this Lecture
    public Pair<Integer, Integer> confusionMarkResolvedCountPair;
}
