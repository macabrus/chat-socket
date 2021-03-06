package com.blogspot.sontx.chatsocket.client.view.javafx;

import com.blogspot.sontx.chatsocket.client.view.FriendListView;
import com.blogspot.sontx.chatsocket.client.view.javafx.custom.FriendListViewCell;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.bean.AccountInfo;
import com.blogspot.sontx.chatsocket.lib.view.BaseJavaFxWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

class FriendListWindow extends BaseJavaFxWindow implements FriendListView {
    private final ObservableList<AccountInfo> friends = FXCollections.observableArrayList();
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private ListView<AccountInfo> friendListView;
    @Setter
    private Runnable myInfoButtonClickListener;
    @Setter
    private Callback<AccountInfo> friendButtonClickListener;

    FriendListWindow() {
        init(this, "friend-list-window.fxml");
        friendListView.setCellFactory(listView -> new FriendListViewCell());
        friendListView.setItems(friends);
    }

    @FXML
    private void onListViewItemClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            AccountInfo selectedItem = friendListView.getSelectionModel().getSelectedItem();
            if (friendButtonClickListener != null && selectedItem != null)
                friendButtonClickListener.call(selectedItem);
        }
    }

    @FXML
    private void onMyInfoClick(MouseEvent event) {
        if (event.getClickCount() == 2 && myInfoButtonClickListener != null)
            myInfoButtonClickListener.run();
    }

    @Override
    public void setFriendList(List<AccountInfo> friendAccountInfoList) {
        friends.addAll(friendAccountInfoList);
    }

    @Override
    public void setMyAccountInfo(AccountInfo accountInfo) {
        if (accountInfo != null) {
            statusLabel.setText(accountInfo.getStatus());
        } else {
            statusLabel.setText("Hi there!");
        }
    }

    @Override
    public void updateFriend(AccountInfo friendInfo) {
        Optional<AccountInfo> matchedFriend = friends
                .stream()
                .filter(accountInfo -> accountInfo.getAccountId() == friendInfo.getAccountId())
                .findFirst();

        matchedFriend.ifPresent(accountInfo -> friends.set(friends.indexOf(accountInfo), friendInfo));
    }

    @Override
    public void addNewFriend(AccountInfo newFriendInfo) {
        friends.add(newFriendInfo);
    }
}
