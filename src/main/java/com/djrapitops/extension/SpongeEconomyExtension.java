/*
    Copyright(c) 2019 Risto Lahtela (Rsl1122)

    The MIT License(MIT)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files(the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions :
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package com.djrapitops.extension;

import com.djrapitops.plan.extension.CallEvents;
import com.djrapitops.plan.extension.DataExtension;
import com.djrapitops.plan.extension.annotation.*;
import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * DataExtension for Sponge economy.
 * <p>
 * Adapted from PluginData implementation by BrainStone.
 *
 * @author Rsl1122
 */
@PluginInfo(name = "Sponge Economy", iconName = "money-bill-wave", iconFamily = Family.SOLID, color = Color.AMBER)
public class SpongeEconomyExtension implements DataExtension {

    private EconomyService eco;

    SpongeEconomyExtension() {
        /* For tests */
    }

    SpongeEconomyExtension(EconomyService eco) {
        this.eco = eco;
    }

    @Override
    public CallEvents[] callExtensionMethodsOn() {
        return new CallEvents[]{
                CallEvents.PLAYER_JOIN,
                CallEvents.PLAYER_JOIN,
                CallEvents.SERVER_EXTENSION_REGISTER
        };
    }

    @BooleanProvider(
            text = "",
            conditionName = "hasAccount",
            hidden = true
    )
    public boolean hasAccount(UUID playerUUID) {
        return eco.hasAccount(playerUUID);
    }

    public Optional<UniqueAccount> getAccount(UUID playerUUID) {
        return eco.getOrCreateAccount(playerUUID);
    }

    @Conditional("hasAccount")
    @DoubleProvider(
            text = "Balance (Default Currency)",
            description = "How much currency the player has.",
            iconName = "money-bill-wave",
            priority = 100,
            iconColor = Color.AMBER,
            showInPlayerTable = true
    )
    public double balance(UUID playerUUID) {
        Currency defaultCurrency = eco.getDefaultCurrency();
        BigDecimal balance = getAccount(playerUUID).map(account -> account.getBalance(defaultCurrency)).orElse(BigDecimal.ZERO);

        return balance.doubleValue();
    }

    @StringProvider(
            text = "Default Currency",
            description = "What currency is used by default.",
            iconName = "money-bill-wave",
            priority = 101,
            iconColor = Color.AMBER
    )
    public String defaultCurrency() {
        return eco.getDefaultCurrency().getDisplayName().toPlain();
    }
}