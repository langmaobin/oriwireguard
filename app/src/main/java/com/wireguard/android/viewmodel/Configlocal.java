/*
 * Copyright © 2017-2018 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wireguard.android.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Parcel;
import android.os.Parcelable;

import com.wireguard.config.Config;
import com.wireguard.config.Interface;
import com.wireguard.config.ParseException;
import com.wireguard.config.Peer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Configlocal implements Parcelable {
    public static final Creator<Configlocal> CREATOR = new ConfigProxyCreator();

    private final Interfacelocal interfaze;
    private final List<Peerlocal> peers = new ArrayList<>();

    private Configlocal(final Parcel in) {
        interfaze = in.readParcelable(Interfacelocal.class.getClassLoader());
        in.readTypedList(peers, Peerlocal.CREATOR);
//        for (final Peerlocal proxy : peers)
//            proxy.bind(this);
    }
//
//    public Configlocal(final Config other) {
//        interfaze = new Interfacelocal(other.getInterface());
//        for (final Peer peer : other.getPeers()) {
//            final Peerlocal proxy = new Peerlocal(peer);
//            peers.add(proxy);
//            proxy.bind(this);
//        }
//    }

    public Configlocal() {
        interfaze = new Interfacelocal();
    }

    public Peerlocal addPeer() {
        final Peerlocal proxy = new Peerlocal();
        peers.add(proxy);
        //proxy.bind(this);
        return proxy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Interfacelocal getInterface() {
        return interfaze;
    }

    public List<Peerlocal> getPeers() {
        return peers;
    }

    public Config resolve() throws ParseException {
        final Collection<Peer> resolvedPeers = new ArrayList<>();
        for (final Peerlocal proxy : peers)
            resolvedPeers.add(proxy.resolve());
        return new Config.Builder()
                .setInterface(interfaze.resolve())
                .addPeers(resolvedPeers)
                .build();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(interfaze, flags);
        dest.writeTypedList(peers);
    }


        private static class ConfigProxyCreator implements Creator<Configlocal> {
            @Override
            public Configlocal createFromParcel(final Parcel in) {
                return new Configlocal(in);
            }
        @Override
        public Configlocal[] newArray(final int size) {
            return new Configlocal[size];
        }
    }
}
