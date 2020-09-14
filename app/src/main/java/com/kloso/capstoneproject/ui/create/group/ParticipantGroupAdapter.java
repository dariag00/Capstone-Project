package com.kloso.capstoneproject.ui.create.group;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kloso.capstoneproject.R;
import com.kloso.capstoneproject.data.model.Participant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantGroupAdapter extends RecyclerView.Adapter<ParticipantGroupAdapter.ParticipantGroupViewHolder> {

    private List<Participant> participantList;

    private ParticipantLongClickListener participantLongClickListener;

    public  ParticipantGroupAdapter(){
        participantList = new ArrayList<>();
    }

    public ParticipantGroupAdapter(ParticipantLongClickListener participantLongClickListener){
        this.participantLongClickListener = participantLongClickListener;
    }


    @NonNull
    @Override
    public ParticipantGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_list_item, parent, false);
        return new ParticipantGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantGroupViewHolder holder, int position) {
        holder.bind(participantList.get(position));
    }

    @Override
    public int getItemCount() {
        return participantList != null ? participantList.size() : 0;
    }

    public void setParticipantList(List<Participant> participantList){
        this.participantList = participantList;
        notifyDataSetChanged();
    }

    class ParticipantGroupViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        @BindView(R.id.tv_participant_name)
        TextView participantNameView;
        @BindView(R.id.profile_image)
        CircleImageView circleImageView;

        public ParticipantGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
        }

        void bind(Participant participant){
            participantNameView.setText(participant.getName());
            if(participant.getProfilePictureUri() != null)
                Picasso.get().load(Uri.parse(participant.getProfilePictureUri())).error(R.drawable.ic_add_black_56dp).into(circleImageView);
        }

        @Override
        public boolean onLongClick(View view) {
            int clickedPosition = getAdapterPosition();

            if(participantLongClickListener != null)
                participantLongClickListener.onParticipantClick(participantList.get(clickedPosition), clickedPosition);

            return true;
        }
    }

    public interface ParticipantLongClickListener {
        void onParticipantClick(Participant clickedParticipant, int clickedPosition);
    }

}
