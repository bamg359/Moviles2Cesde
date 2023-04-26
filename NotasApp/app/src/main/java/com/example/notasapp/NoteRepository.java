package com.example.notasapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {


    private NoteDao noteDao;
    private LiveData<List<Note>> notes;

    ExecutorService executors = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application){


        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        notes = noteDao.getAllNotes();

    }


    public void insert(Note note){

        executors.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });

    }

    public void update(Note note){

        executors.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);

            }
        });



    }


    public void delete(Note note){

        executors.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });



    }

    public LiveData<List<Note>> getAllNotes(){

        return notes;
    }





    private static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{



        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){

            this.noteDao = noteDao;



        }


        @Override
        protected Void doInBackground(Note... notes) {

            noteDao.insert(notes[0]);
            return null;
        }
    }



}
