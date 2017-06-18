package com.example.yuichi_oba.ostraca.tools;

/**
 * Created by Yuichi-Oba on 2017/06/18.
 */

//public class MyListAdapter  extends RecyclerView.Adapter<MyViewHolder>{
//
//    private static final String TAG = MyListAdapter.class.getSimpleName();
//    private List<ListItem> data;
//
//    /********************************
//     *  Constractor
//     *******************************/
//    public MyListAdapter(List<ListItem> data) {
//        this.data = data;
//    }
//
//    /********************************
//     *  OverRide Method
//     *******************************/
//    // ビューホルダーを生成
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        /*****************/
//            /*  // TODO: 2017/06/18  ビューをクリックしたロジックはここへ実装   */
//        /*****************/
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView stu_id = (TextView) view.findViewById(R.id.stu_id);
//                RadioButton rbn_1 = (RadioButton) view.findViewById(R.id.rb_1);
//                RadioButton rbn_2 = (RadioButton) view.findViewById(R.id.rb_2);
//                RadioButton rbn_3 = (RadioButton) view.findViewById(R.id.rb_3);
//                RadioButton rbn_4 = (RadioButton) view.findViewById(R.id.rb_4);
//
//                Log.d(TAG, stu_id.getText().toString());
//                Log.d(TAG, rbn_1.isChecked() + " : " + rbn_2.isChecked() + " : " + rbn_3.isChecked() + " : " + rbn_4.isChecked());
//
//            }
//        });
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.stu_id.setText(data.get(position).getStu_id());
//        holder.stu_name.setText(data.get(position).getStu_name());
//        holder.rb_1.setSelected(data.get(position).getAttend()[0]);
//        holder.rb_2.setSelected(data.get(position).getAttend()[1]);
//        holder.rb_3.setSelected(data.get(position).getAttend()[2]);
//        holder.rb_4.setSelected(data.get(position).getAttend()[3]);
//    }
//
//    @Override
//    public int getItemCount() {
//        return this.data.size();
//    }
//
//    public List<ListItem> getData() {
//        return data;
//    }
//
//    public void setData(List<ListItem> data) {
//        this.data = data;
//    }
//}
