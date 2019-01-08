package be.ehb.dig_x.ricardo.werkstuk_android.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import be.ehb.dig_x.ricardo.werkstuk_android.Fragments.ShoesFragment;
import be.ehb.dig_x.ricardo.werkstuk_android.Model.Shoe;
import be.ehb.dig_x.ricardo.werkstuk_android.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ViewHolder>{

    private Context mContext;
    private List<Shoe> shoeList;

    public ShoeAdapter(Context mContext, List<Shoe> shoeList) {
        this.mContext = mContext;
        this.shoeList = shoeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.shoe_item,viewGroup,false);

        return new ShoeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Shoe shoe = shoeList.get(i);

        viewHolder.model_name.setText(shoe.getModelName());
        viewHolder.colorway.setText(shoe.getColorway());
        viewHolder.model_number.setText(shoe.getModelNr());
        Glide.with(mContext).load(shoe.getImageUrl()).into(viewHolder.shoe_image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("modelnr",shoe.getModelNr());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ShoesFragment()).commit();
            }
        });



    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

    public CircleImageView shoe_image;
    public TextView model_name,colorway, model_number;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        model_name = itemView.findViewById(R.id.model_name);
        shoe_image = itemView.findViewById(R.id.shoe_image);
        colorway = itemView.findViewById(R.id.colorway);
        model_number = itemView.findViewById(R.id.model_number);

    }
}


}
