package com.kloso.capstoneproject.ui.create.expense;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.Participant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsAdapterViewHolder> {

    private List<Participant> participantList;
    private List<Participant> selectedParticipants;
    private boolean blockRealUsers;

    public ParticipantsAdapter(List<Participant> participantList) {
        this.participantList = participantList;
        this.selectedParticipants = participantList;
        this.blockRealUsers = false;
    }

    public ParticipantsAdapter(List<Participant> participantList, boolean blockRealUsers) {
        this.participantList = participantList;
        if(blockRealUsers)
            this.selectedParticipants = new ArrayList<>();
        this.blockRealUsers = blockRealUsers;
        notifyDataSetChanged();
    }


    public void setParticipantList(List<Participant> participantList){
        this.participantList = participantList;
        notifyDataSetChanged();
    }

    public List<Participant> getSelectedParticipants() {
        return selectedParticipants;
    }

    @NonNull
    @Override
    public ParticipantsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_checkbox_item, parent, false);
        return new ParticipantsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsAdapterViewHolder holder, int position) {
        holder.bind(participantList.get(position));
    }

    @Override
    public int getItemCount() {
        return participantList != null ? participantList.size() : 0;
    }

    class ParticipantsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.checkbox_participant)
        CheckBox checkBox;
        private Participant currentParticipant;

        public ParticipantsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if(blockRealUsers) checkBox.setChecked(false);
            itemView.setOnClickListener(this);
        }

        public void bind(Participant participant){
            this.currentParticipant = participant;
            checkBox.setText(participant.getName());
            if(blockRealUsers && participant.isRealUser()) {
                checkBox.setClickable(false);
                //checkBox.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.grayBackground));
                checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                checkBox.setText(checkBox.getText().toString().concat(" (Already associated)"));
            }
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(checkBox.isChecked() && !selectedParticipants.contains(currentParticipant)){
                selectedParticipants.add(currentParticipant);
            } else {
                selectedParticipants.remove(currentParticipant);
            }
        }
    }

}
