package com.example.conwayying.query.data;

import android.util.Pair;

import com.example.conwayying.query.data.entity.AcademicClass;

/**
 * Composition of AcademicClass and some information relevant to displaying an AcademicClass
 *  in a list
 */
public class AcademicClassDataEntry {
    // An AcademicClass
    public AcademicClass academicClass;
    // First integer is number of resolved notes, second integer is number of unresolved notes
    //  for this AcademicClass
    public Pair<Integer, Integer> noteResolvedCountPair;
    // First integer is number of resolved confusion marks, second integer is number of unresolved confusion marks
    //  for this AcademicClass
    public Pair<Integer, Integer> confusionMarkResolvedCountPair;
}
