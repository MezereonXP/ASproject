package com.checkfile.checkfile.View;

import java.util.ArrayList;
import java.util.List;

import com.checkfile.*;
import com.checkfile.checkfile.Adapter.LogisticsAdapter;
import com.checkfile.checkfile.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class CustomNodeListView extends LinearLayout {

	private CustomNodeLineView nodeLineView;
	private CustomListView listView;
	private BaseAdapter adapter;
	private List<Float> nodeRadiusDistances;


	public CustomNodeListView(Context context) {
		this(context, null);
	}

	public CustomNodeListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public CustomNodeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.custom_node_listview_layout, this);
		this.setNodeLineView((CustomNodeLineView) this
				.findViewById(R.id.nodeLineView));
		this.listView = (CustomListView) this.findViewById(R.id.CustomListView);
		this.listView.setEnabled(false);
		this.nodeRadiusDistances = new ArrayList<Float>();
	}

	public void setTopNodePaintStrokeWidth(float topNodePaintStrokeWidth) {
		if (this.getNodeLineView() != null) {
			this.getNodeLineView()
					.setTopNodePaintStrokeWidth(topNodePaintStrokeWidth);
		}
		invalidate();
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter; 
		this.listView.setAdapter(adapter);
		this.nodeRadiusDistances = ((LogisticsAdapter) adapter).getNodeRadiusDistances();
		this.nodeRadiusDistances.set(this.nodeRadiusDistances.size() - 1, (float)this.listView.getDividerHeight());
		this.setNodeCount(adapter.getCount());
		this.setNodeRadiusDistances(nodeRadiusDistances);
		invalidate();
	}

	private void setNodeRadiusDistances(List<Float> nodeRadiusDistances) {
		this.getNodeLineView().setNodeRadiusDistances(nodeRadiusDistances);
		invalidate();
	}

	private void setNodeCount(int nodeCount) {
		this.getNodeLineView().setNodeCount(nodeCount);
		invalidate();
	}
	
	public void setItemPaddingTop(int itemPaddingTop) {
		this.getNodeLineView().setItemPaddingTop(itemPaddingTop);
		invalidate();
	}
	
	public void addHeaderView(View view) {
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(view, 0);
		invalidate();
	}

	public CustomNodeLineView getNodeLineView() {
		return nodeLineView;
	}

	public void setNodeLineView(CustomNodeLineView nodeLineView) {
		this.nodeLineView = nodeLineView;
	}
}
