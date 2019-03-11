package com.dapps.springresttemplate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dapps.springresttemplate.R;
import com.dapps.springresttemplate.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private Context mContext;
    private List<Contact> contactList;

    public ContactAdapter(Context mContext, List<Contact> responseContactList) {
        this.mContext = mContext;
        this.contactList = responseContactList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder contactHolder, int i) {

        contactHolder.text_name.setText(contactList.get(i).getName());
        contactHolder.text_email.setText(contactList.get(i).getEmail());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_email;

        ContactHolder(@NonNull View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_email = itemView.findViewById(R.id.text_email);
        }
    }
}
