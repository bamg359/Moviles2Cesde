package com.example.notasapp;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){

        if (instance == null){


            instance= Room.databaseBuilder(context.getApplicationContext()
                    ,NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }


        return instance;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){

            super.onCreate(db);

            NoteDao noteDao = instance.noteDao();

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {


                    noteDao.insert(new Note("titulo1","descripcion1"));
                    noteDao.insert(new Note("titulo2","descripcion1"));
                    noteDao.insert(new Note("titulo3","descripcion1"));
                    noteDao.insert(new Note("titulo4","descripcion1"));
                    noteDao.insert(new Note("titulo5","descripcion1"));
                    noteDao.insert(new Note("titulo6","descripcion1"));

                }
            });


        }




    };



}
