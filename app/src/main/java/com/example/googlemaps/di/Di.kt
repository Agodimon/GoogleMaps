package com.example.googlemaps.di

import com.example.googlemaps.di.modules.RoomModule
import com.example.googlemaps.di.modules.RoomModuleInt
import org.koin.dsl.module

object Di {
    val mainModule =module {
        factory<RoomModuleInt> { RoomModule() }
    }
}