package com.example.contactapp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable {
    private List<Contact> contacts;
    private List<Contact> oldContacts;
    private ItemClickListener listener;

    public interface ItemClickListener {
        void onItemClick(Contact item);
    }

    public ContactsAdapter(List<Contact> contacts, ItemClickListener itemClickListener) {
        this.contacts = contacts;
        this.oldContacts = contacts;
        this.listener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        load avatar
        Glide
                .with(holder.ivAvatar.getContext())
                .load(Uri.parse(contacts.get(position).getAvatarUri()))
                .into(holder.ivAvatar);

//        load initial
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact, Contact t1) {
                return contact.getName().compareToIgnoreCase(t1.getName());
            }
        });
        if (position != 0 && contacts.get(position).getName().charAt(0) == contacts.get(position-1).getName().charAt(0)) {
            holder.tvInitial.setVisibility(View.INVISIBLE);
        } else {
            holder.tvInitial.setText(Character.toString(contacts.get(position).getName().charAt(0)));
        }

//        load name
        holder.tvName.setText(contacts.get(position).getName());

//        on click contact item
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(contacts.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if (search.isEmpty()) {
                    contacts = oldContacts;
                } else {
                    List<Contact> contactList = new ArrayList<>();
                    for (Contact contact : oldContacts) {
                        if (contact.getName().toLowerCase().contains(search.toLowerCase())) {
                            contactList.add(contact);
                        }
                    }

                    contacts = contactList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contacts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contacts = (List<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cvItem;
        public TextView tvName;
        public ImageView ivAvatar;
        public TextView tvInitial;

        public ViewHolder(View view) {
            super(view);
            cvItem = view.findViewById(R.id.cv_item);
            tvName = view.findViewById(R.id.tv_name);
            ivAvatar = view.findViewById(R.id.iv_avatar);
            tvInitial = view.findViewById(R.id.tv_initial);
        }
    }
}
