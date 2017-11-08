package com.dev.mendes.android_mytasks.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dev.mendes.android_mytasks.R;
import com.dev.mendes.android_mytasks.activity.MemorService;
import com.dev.mendes.android_mytasks.activity.ModTaskActivity;
import com.dev.mendes.android_mytasks.activity.TaskListActivity;
import com.dev.mendes.android_mytasks.adapter.TaskListAdapter;
import com.dev.mendes.android_mytasks.dataBase.DataBaseControl;
import com.dev.mendes.android_mytasks.dataBase.Task;


public class TaskListFragment extends Fragment implements TaskListAdapter.OnTaskClickInterface {

    private TaskListAdapter adapter;
    private LinearLayout layoutEmpty;

    private  String titulo, texto;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Iniciando MemorService
        MemorService service = new MemorService("MemorService", getContext());

        // Infla o layout correto
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Enconta o layout de lista vazia
        layoutEmpty = (LinearLayout) view.findViewById(R.id.layout_empty);

        // Busca a RV da tela e define seu layout
        RecyclerView listaTarefas = (RecyclerView) view.findViewById(R.id.rv);
        listaTarefas.setLayoutManager(new LinearLayoutManager(getContext()));

        // Define o adapter da RV
        adapter = new TaskListAdapter(getContext(), this);
        listaTarefas.setAdapter(adapter);

        // CLick do fab
        FloatingActionButton fabAdd = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ModTaskActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onTaskClick(Task item) {
        Intent intent = new Intent(getContext(), ModTaskActivity.class);
        intent.putExtra(ModTaskActivity.EXTRA_TASK, item);
        startActivity(intent);
    }

    private void loadTasksFromBD() {
        // Busca tarefas do bd
        DataBaseControl crud = new DataBaseControl(getContext());
        Cursor cursor = crud.loadTasks();
        // Envia apontador para o adapter
        adapter.swapCursor(cursor);

        // Seta o layout da lista se vazia
        if (cursor.getCount() == 0) {
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Atualiza a lista quando retorna a tela
        loadTasksFromBD();


    }
}
