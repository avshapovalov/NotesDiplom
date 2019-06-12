package com.andrognito.notesfordiplom;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NoteRepository {

    public static final int ACTION_NEW_NOTE = 0;
    public static final int ACTION_UPDATE = 1;
    private static final String JSON_REPOSITORY_NAME = "noteRepository";
    private static final String JSON_REPOSITORY_KEY = "noteRepository";
    private List<Note> noteList;
    public static final String TAG = "LOG";
    private Gson gson;

    public NoteRepository() {
    }

    public void saveNote(Context context, Note note) {
        Log.d(TAG, "note = " + note.toString());
        noteList = fillList(context);
        Log.d(TAG, "noteList = " + noteList.toString());
        noteList.add(note);

        SharedPreferences sharedPreferences = context.getSharedPreferences(JSON_REPOSITORY_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();
        String json = gson.toJson(noteList);
        Log.d(TAG, "json = " + json);
        editor.putString(JSON_REPOSITORY_KEY, json);
        editor.commit();
        Log.d(TAG, "Сохранились");
        Toast.makeText(context, "Заметка сохранена", Toast.LENGTH_LONG).show();
    }

    public List<Note> fillList(Context context) {
        gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(JSON_REPOSITORY_NAME, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(JSON_REPOSITORY_KEY, "");
        Log.d(TAG, "jsonPreferences = " + jsonPreferences);
        Type type = new TypeToken<List<Note>>() {
        }.getType();
        Log.d(TAG, "type = " + type);
        try {
            Log.d(TAG, "Начинаем заполнять список = ");
            noteList = new ArrayList<>();
            noteList = gson.fromJson(jsonPreferences, type);
            Log.d(TAG, "noteList = " + noteList.toString());
        } catch (NullPointerException e) {
            noteList = new ArrayList<>();
        }
        return noteList;
    }

    public void deleteNote(Context Context, Note note, NotesAdapter notesAdapter, int position) {
        noteList = fillList(Context);
        for (int i = 0; i < noteList.size(); i++) {
            if (noteList.get(i).getCreationDate().equals(note.getCreationDate())) {
                noteList.remove(i);
            }
        }

        SharedPreferences sharedPreferences = Context.getSharedPreferences(JSON_REPOSITORY_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();
        String json = gson.toJson(noteList);
        editor.putString(JSON_REPOSITORY_KEY, json);
        editor.commit();
        notesAdapter.notifyItemRemoved(position);
        Toast.makeText(Context, "Заметка удалена", Toast.LENGTH_SHORT).show();
    }

    public void updateNote(Context context, Note note) {
        noteList = fillList(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(JSON_REPOSITORY_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < noteList.size(); i++) {
            if (noteList.get(i).getCreationDate().equals(note.getCreationDate())) {
                noteList.get(i).setNoteTitle(note.getNoteTitle());
                noteList.get(i).setNoteDescription(note.getNoteDescription());
                noteList.get(i).setNoteTime(note.getNoteTime());
                noteList.get(i).setChangeDate(note.getChangeDate());
                noteList.get(i).setDeadlineNeeded(note.getDeadlineNeeded());
            }
        }

        gson = new Gson();
        String json = gson.toJson(noteList);
        editor.putString(JSON_REPOSITORY_KEY, json);
        editor.commit();

        Toast.makeText(context, "Заметка обновлена", Toast.LENGTH_SHORT).show();
    }

}
