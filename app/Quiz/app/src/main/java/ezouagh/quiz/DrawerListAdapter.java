package ezouagh.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Younes on 13-Aug-15.
 */
public class DrawerListAdapter extends ArrayAdapter<questionLbl> {

    private  static class ViewHolder{
        TextView txt;
        LinearLayout r;
    }

    public DrawerListAdapter(Context context ,ArrayList<questionLbl> qsLabels) {
      super(context, R.layout.questions_menu_list_item,
              R.id.Menu_lblListItem, qsLabels);
   }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        questionLbl q=getItem(position);
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            LayoutInflater inflater =LayoutInflater.from(getContext());
            convertView=inflater.inflate( R.layout.questions_menu_list_item,parent,false);
            viewHolder.txt= (TextView) convertView.findViewById( R.id.Menu_lblListItem);
            viewHolder.r= (LinearLayout) convertView.findViewById( R.id.ReviewListItem);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.txt.setText(q.lbl);
        viewHolder.r.setBackgroundResource(q.colorId);
        return  convertView;
    }
}
