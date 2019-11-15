package com.iejag.countit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ItemViewHolder> {
    private Context context;
    private List<Product> items;
    private ProductsAdapterListener listener;

    public ProductsAdapter(Context context, List<Product> items, ProductsAdapterListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View template = inflater.inflate(R.layout.template_list_item, parent, false);
        return new ProductsAdapter.ItemViewHolder(template);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ItemViewHolder holder, int position) {
        final Product product= items.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.editUser(product.getId());

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteUser(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateProducts(List<Product> products) {
        this.items = products;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvPrice;
        private ImageView delete;
        private ImageView edit;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nombre);
            tvPrice = itemView.findViewById(R.id.tv_price);
            delete = itemView.findViewById(R.id.iv_delete);
            edit = itemView.findViewById(R.id.iv_edit);
        }
    }
    public interface ProductsAdapterListener {

        void deleteUser(String id);

        void editUser(String id);
    }

}
