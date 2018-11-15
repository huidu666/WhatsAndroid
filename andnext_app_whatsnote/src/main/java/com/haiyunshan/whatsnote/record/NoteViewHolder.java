package com.haiyunshan.whatsnote.record;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import club.andnext.recyclerview.swipe.SwipeHolder;
import club.andnext.recyclerview.swipe.SwipeViewHolder;
import club.andnext.recyclerview.swipe.runner.RightActionRunner;
import club.andnext.utils.PrettyTimeUtils;
import com.haiyunshan.whatsnote.record.entity.RecordEntity;
import com.haiyunshan.whatsnote.R;
import com.haiyunshan.whatsnote.ShowRecordActivity;

class NoteViewHolder<F extends BaseRecordFragment> extends SwipeViewHolder<RecordEntity> implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {

    static final int LAYOUT_RES_ID = R.layout.layout_note_file_list_item;

    View contentLayout;
    ImageView iconView;
    TextView nameView;
    TextView infoView;

    View deleteBtn;

    F parent;

    public NoteViewHolder(F f, View itemView) {
        super(itemView);

        this.parent = f;
    }

    @Override
    public int getLayoutResourceId() {
        return LAYOUT_RES_ID;
    }

    @Override
    public void onViewCreated(@NonNull View view) {

        {
            this.contentLayout = view.findViewById(R.id.content_layout);
            contentLayout.setOnClickListener(this);
            contentLayout.setOnLongClickListener(this);

            this.iconView = view.findViewById(R.id.iv_icon);
            this.nameView = view.findViewById(R.id.tv_name);
            this.infoView = view.findViewById(R.id.tv_info);
        }

        {
            this.deleteBtn = view.findViewById(R.id.btn_delete);
            deleteBtn.setOnClickListener(this);
        }

        {
            SwipeHolder holder = new SwipeHolder(parent.swipeActionHelper, view, contentLayout);

            {
                RightActionRunner r = new RightActionRunner(deleteBtn);
                holder.add(r);
            }

            this.setSwipeHolder(holder);
        }

    }

    @Override
    @CallSuper
    public void onBind(RecordEntity item, int position) {

        iconView.setImageResource(item.isDirectory()? R.drawable.ic_folder_white_24dp: R.drawable.ic_note_white_24dp);

        nameView.setText(item.getName());

        infoView.setText(PrettyTimeUtils.format(item.getCreated().toDate()));
    }

    @Override
    public void onClick(View v) {
        parent.swipeActionHelper.clear();

        if (v == contentLayout) {
            RecordEntity entity = getItem();
            if (entity.isDirectory()) {
                ShowRecordActivity.start(parent.getActivity(), getItem().getId());
            } else {
                parent.requestCompose(entity);
            }
        } else if (v == deleteBtn) {
            parent.requestDelete(getItem());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == contentLayout) {
            popupMenu();
            return true;
        }

        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_rename: {
                parent.requestRename(getItem());
                break;
            }
            case R.id.menu_tag: {
                parent.requestTag(getItem());
                break;
            }
            case R.id.menu_favorite: {
                parent.requestFavorite(getItem());
                break;
            }
            case R.id.menu_move: {
                parent.requestMove(getItem());
                break;
            }
        }

        return false;
    }

    void popupMenu() {
        PopupMenu popup = new PopupMenu(parent.getActivity(), itemView);
        popup.inflate(R.menu.menu_record_list_item);
        popup.setOnMenuItemClickListener(this);

        if (getItem().isTrash() || !getItem().isDirectory()) {
            popup.getMenu().findItem(R.id.menu_favorite).setVisible(false);
        }

        if (getItem().isTrash()) {
            popup.getMenu().findItem(R.id.menu_move).setVisible(false);
        }

        popup.show();
    }
}
