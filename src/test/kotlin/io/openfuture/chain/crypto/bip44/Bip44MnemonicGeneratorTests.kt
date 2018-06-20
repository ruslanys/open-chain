package io.openfuture.chain.crypto.bip44

import io.openfuture.chain.config.ServiceTests
import io.openfuture.chain.config.any
import io.openfuture.chain.crypto.bip39.dictionary.WordList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock

class Bip44MnemonicGeneratorTests : ServiceTests() {

    @Mock private lateinit var wordList: WordList

    @InjectMocks
    private lateinit var bip44MnemonicGenerator: Bip44MnemonicGenerator

    @Test
    fun createMnemonicWhenByteArrayIsTwelveBytesShouldReturnSeedPhrase() {
        var entropy = ByteArray(16)
        var word = "1"
        var expectedWords = "1 1 1 1 1 1 1 1 1 1 1 1"

        given(wordList.getSeparator()).willReturn(' ')
        given(wordList.getWord(any(Int::class.java))).willReturn(word)

        var seedPhraseResult = bip44MnemonicGenerator.createMnemonic(entropy)

        assertThat(seedPhraseResult).isEqualTo(expectedWords)
    }
}
