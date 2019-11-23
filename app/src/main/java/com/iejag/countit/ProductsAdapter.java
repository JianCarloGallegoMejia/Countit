package com.iejag.countit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ItemViewHolder>  {
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
        holder.tvPrice.setText(product.getQuantity()+" X "+product.getPrice()+" = "+ String.format(product.getTotal()));
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.editProduct(product.getId());

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteProduct(product.getId());
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

    public void setProducts(List<Product> productsFiltered) {
        this.items = productsFiltered;
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

        void deleteProduct(String id);

        void editProduct(String id);
    }

}
