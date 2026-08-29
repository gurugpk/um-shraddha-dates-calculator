package com.shraddhacalendar.core.tradition

import com.shraddhacalendar.core.models.MadhwaTradition

/**
 * Factory to obtain the appropriate tradition-specific calculation engine.
 */
object TraditionEngineFactory {

    private val umEngine = UttaradiMathaTraditionEngine()
    private val srsEngine = MantralayaTraditionEngine()
    private val udupiEngine = UdupiAshtaMathaTraditionEngine()

    fun getEngine(tradition: MadhwaTradition): IShraddhaTraditionEngine {
        return when (tradition) {
            MadhwaTradition.UTTARADI_MATHA -> umEngine
            MadhwaTradition.MANTRALAYA_MUTT -> srsEngine
            MadhwaTradition.UDUPI_ASHTA_MATHA -> udupiEngine
        }
    }
}
