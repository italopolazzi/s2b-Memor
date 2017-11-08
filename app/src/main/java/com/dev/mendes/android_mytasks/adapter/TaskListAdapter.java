package com.dev.mendes.android_mytasks.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.mendes.android_mytasks.R;
import com.dev.mendes.android_mytasks.dataBase.DataBaseControl;
import com.dev.mendes.android_mytasks.dataBase.DataBaseTask;
import com.dev.mendes.android_mytasks.dataBase.Task;

/**
 * Created by tainaviriato on 21/06/17.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private Cursor mCursor;
    private OnTaskClickInterface clickInterface;
    private Context context;

    public TaskListAdapter(Context context, OnTaskClickInterface onTaskClickInterface) {
        this.context = context;
        this.clickInterface = onTaskClickInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla o layout correto
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Recupera os valores da lista na posicao atual
        // Mapeia o index do banco de dados
        final int idIndex = mCursor.getColumnIndex(DataBaseTask.ID);
        int nameIndex = mCursor.getColumnIndex(DataBaseTask.NOME);
        int checkIndex = mCursor.getColumnIndex(DataBaseTask.CHECK);

        // Move o cursor do bd para a posicaco
        mCursor.moveToPosition(position);

        // Mapeia os dados no item da lista
        holder.nameTask.setText(mCursor.getString(nameIndex));
        holder.checkBox.setChecked(mCursor.getInt(checkIndex) == 1);


        // Deleta tarefa ao clicar no delete para cada item da lista
        holder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Busca o banco de dados
                DataBaseControl db = new DataBaseControl(holder.itemView.getContext());
                db.deleteTask(mCursor.getInt(idIndex));
                // Atualiza a lista
                mCursor = db.loadTasks();
                notifyDataSetChanged();
            }
        });


        // Click do check box para cada item da lista
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Move o cursor para posicao recebida
                mCursor.moveToPosition(holder.getAdapterPosition());
                // Busca o banco de dados
                DataBaseControl db = new DataBaseControl(holder.itemView.getContext());
                db.setChecked(mCursor.getString(idIndex), isChecked);
                // Risca o texto se checkbox for selecionado
                if(isChecked ){
                    holder.nameTask.setPaintFlags(holder.nameTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.nameTask.setPaintFlags(holder.nameTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }


            }
        });

        // Click para cada item da lista
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onTaskClick(getTaskFromCursor(holder.getAdapterPosition()));
            }
        });


    }

    /*
     * Converte o cursor em uma tarefa
     */
    private Task getTaskFromCursor(int position) {
        // Move cursor para posicao recebida
        mCursor.moveToPosition(position);

        // Mapeia o index do banco de dados
        int idIndex = mCursor.getColumnIndex(DataBaseTask.ID);
        int nameIndex = mCursor.getColumnIndex(DataBaseTask.NOME);
        int placeIndex = mCursor.getColumnIndex(DataBaseTask.PLACE);
        int coordinatesIndex = mCursor.getColumnIndex(DataBaseTask.COORDINATES);
        int dateIndex = mCursor.getColumnIndex(DataBaseTask.DATE);
        int checkIndex = mCursor.getColumnIndex(DataBaseTask.CHECK);
        int noteIndex = mCursor.getColumnIndex(DataBaseTask.NOTE);

        // Adiciona na tarefa as informacoes recuperadas
        Task task = new Task();
        task.setId(mCursor.getString(idIndex));
        task.setTaskName(mCursor.getString(nameIndex));
        task.setTaskPlace(mCursor.getString(placeIndex));
        task.setCoordinates(mCursor.getString(coordinatesIndex));
        task.setTaskDate(mCursor.getString(dateIndex));
        task.setTaskNote(mCursor.getString(noteIndex));
        task.setChecked(mCursor.getInt(checkIndex) == 1);

        return task;

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /*
     * Atualiza o cursor
     */
    public void swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return; // bc nothing has changed
        } else {
            mCursor = c;
            notifyDataSetChanged();
        }

    }

    /*
     * Objeto necessario para mapear os componentes do layout do item da lista
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView nameTask;
        private CheckBox checkBox;
        private ImageView icDelete;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.nameTask = (TextView) view.findViewById(R.id.tv_itemList);
            this.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            this.icDelete = (ImageView) view.findViewById(R.id.imgDelete);
        }
    }

    /*
    * Interface para o click da tarefa
     */
    public interface OnTaskClickInterface {
        void onTaskClick(Task item);
    }
}
