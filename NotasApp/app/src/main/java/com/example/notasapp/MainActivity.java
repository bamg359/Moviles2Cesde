package com.example.notasapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    ActivityResultLauncher<Intent> activityResultLauncherAgregarNota;
    ActivityResultLauncher<Intent> activityResultLauncherEditarNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerActivityAgregarNota();
        registerActivityEditarNota();

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NoteAdapter noteAdapter = new NoteAdapter();

        recyclerView.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setNotes(notes);
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(noteAdapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(),"Nota Eliminada", Toast.LENGTH_LONG).show();

            }
        }).attachToRecyclerView(recyclerView);


        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class );
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("title", note.getDescription());

                activityResultLauncherEditarNota.launch(intent);

            }
        });




    }

    @Override
    public boolean onCreateOptionMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_nuevo, menu);

        return true;
    }


    @Override
    public  boolean onOptionItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){

            case R.id.top_menu:

                Intent intent = new Intent(MainActivity.this, AgregarNotaActivity.class);

                activityResultLauncherAgregarNota.launch(intent);

                return true;
            default:
                return  super.onOptionsItemSelected(item);

        }


    }

    public  void registerActivityEditarNota() {

        activityResultLauncherEditarNota = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();


                        if (resultCode == RESULT_OK && data != null) {

                            String title = data.getStringExtra("titleNew");
                            String description = data.getStringExtra("descriptionNew");
                            int id = data.getIntExtra("notaId", -1);

                            Note note = new Note(title, description);
                            note.setId(id);
                            noteViewModel.update(note);

                        }
                    }
                });
    }


        public void registerActivityAgregarNota(){

            activityResultLauncherAgregarNota = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {

                            int codigoResultado= result.getResultCode();
                            Intent data = result.getData();


                            if (codigoResultado == RESULT_OK && data != null){

                                String title = data.getStringExtra("tituloNota");
                                String description = data.getStringExtra("descripcionNota");

                                Note note = new Note(title, description);
                                noteViewModel.insert(note);

                            }

                        }
                    });






        }




    }











}