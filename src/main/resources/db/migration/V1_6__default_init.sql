INSERT INTO blocks (id, TIMESTAMP, height, previous_hash, hash, signature, public_key)
VALUES (1,
        1532345018021,
        1,
        '',
        '838c84179c7e644cdf2ff0af3055ed45c6f43e0bd7634f8bd6ae7d088b1aaf0a',
        'MEUCIQCLeQuqCrDd8nmS037ZfmQNtpUf/AsfQilmK7CcNNIi7QIgKNdhszih/PezHW52v4/tdsZxaLovJzDnLvUy98tnsgg=',
        '038bbbeeb867b999991cd5b146b392ba2fe44ea69d1cc7208e32190184b13aaf1b');

INSERT INTO genesis_blocks (id, epoch_index)
VALUES (1, 1);

INSERT INTO wallets (id, address, balance)
VALUES (1, '0x0000000000000000000000000000000000000000', 10000000000000000);

INSERT INTO delegates (id, public_key, node_id, address, host, port, registration_date)
VALUES (1, '02b04aa1832e799503000a6b8da1cdbb737c167fc829472c726a12ad9a4ccf24eb',
       'e1980f62f593c33d293a3c40e1a23d4624ebd2efaa7e0e4ab6251c55890d8f68',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.194.75.244', 9190, 1532345018021),
       (2, '02c6847fcdc0239581151d1b05e7004c331eba097ae381349220c7cb0c5f5df9b3',
       '9e5a3a23642e28b59e15e9c002abb43a136d609dd01cc48d1ac86bd73ecbba2b',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.186.190.72', 9190, 1532345018021),
       (3, '02c4aedc4a7e2d8cc0e73e6dfb428e8555adc8a1b74207cb61143babd3e04be63f',
       'dd8b9747571f6195b71ab1e1808c4e8ba931047088c071cf8631fac37c506b00',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.188.230.154', 9190, 1532345018021),
       (4, '02203492b48445da0f7392f6fa88d902f71d1b3714740ed99f43009a70fd7f6da8',
       'f7f2261208c8f9c95c9fe795fa0d163542dbeafb21835d6a153356901d5cf8cd',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.199.58.74', 9190, 1532345018021),
       (5, '029a9b6a44d2e322af6884a00660d63ab80effceb0a80f86bd7b21fbf5ee1550ac',
       '6fea4cf9aacd52f8d103d16665c638b52ba74ebc23bc516d87d3bb1e18f7bc18',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.230.166.77', 9190, 1532345018021),
       (6, '020c08e5367fd881e52af43532db814d371b6bd3effb14442ad1044e71c0c0e41a',
       'c45e0be2ae0d297d763ed3671cd0b462001cf2f568e91da055f48038c952e6bf',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.230.168.111', 9190, 1532345018021),
       (7, '02aef406b4c4a3c007094a05c2d2a2d815133a41914c96385a2d9ca71529b4d302',
       '4dd4f38cb86f42f7293e265c1fc8452bd179666c5037aaa36bb77686fe2532f0',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.194.88.166', 9190, 1532345018021),
       (8, '03bfcc7afddf4f00c043faca2254ca8f09e3109c20b830d44a9b4438b363b9865e',
       'a412b38431dc2eb61f5a6fe9b8fdd4e65df458a1ab9c9b1b812a412f09feb6cc',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.221.53.164', 9190, 1532345018021),
       (9, '03b49d9a127c271fad4bcdf88bd9fb3430b122044972654dfe78a754c5e3064f4f',
       'e183c1f9a0300ce07421fb89fa4e2a873076544a76e5156e128b72800a2d5c65',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.236.216.126', 9190, 1532345018021),
       (10, '03679e387bae8b7b724edc42a8149b7aa426edfc9ad54a1fc5e717ab081aca4daf',
       '6317cf00c355586ee7ebba9a4d366c0084f1b37473c98a6677436957c4b9d7e4',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.199.11.229', 9190, 1532345018021),
       (11, '036a1a1a6e952083beb1eb5213168288592cd000b42502bd4b8b1e74a465a2eacc',
       'e6103ec11c36229c67b7a6fc6dadf06517ff97fff226105626eaf88f34ac0d39',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.194.84.89', 9190, 1532345018021),
       (12, '029137a16dcea3967e8fd46dff0d812a2e60a57bef3eb6a7007867c0496631c5d6',
       '55623deba3648ec9941a67c56efd81d999124cc427a3515266d8cadb36fd2e0b',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.221.41.160', 9190, 1532345018021),
       (13, '03a9623189c1da22cec1338d2ab0a982e51794aefb45107d7c4c000a09fc772204',
       'df8c6bcc342805d607d2562c716d8557336f9ddb0821593635b326208e72c08b',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.221.26.81', 9190, 1532345018021),
       (14, '0283d909d2a886e9274f76f0460625e72674222b6a2bc937071858aa76a6e08d78',
       'e7b7483f7d88f5f586c9be0b1dcf7a9ec8ed6cb2b6e5d5b707e7b89912c3f5f6',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.199.50.11', 9190, 1532345018021),
       (15, '02f8f3aca6fbf37e7dfd4cf55cf6a1dcffa2cc6cb0c2e513f8121dfb4d861bf04e',
       'ec331a000e0c6cf952b5f67e29f6602359ab3ade08e160e8f77a3d7e7c4d625e',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.221.58.68', 9190, 1532345018021),
       (16, '039745d56241820f2a385c77aca013ecdff0b9fdce01d3f45ed34752cc9aa62cda',
       '347ad5825c17c8417def650abbb3348cc0c3a1683d50698df37c982060b3612c',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.221.48.140', 9190, 1532345018021),
       (17, '02e7cb6589255a6e153d181c19aa8a34c5c0e6cef0c0374e0c8ba4b5f36ccfc18a',
       'c8a83b48bbd2987f2b37192252f0ab6a58ffb1767f1420ba0c51a1f79d66b946',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.194.81.15', 9190, 1532345018021),
       (18, '02c2aca26e916926fce4101f0633009ae1c8c97e3081b3779880f6683ea258599c',
       '59e109f01c3203873a04d18eed9014075e836aa0ad46687ec7f331a44d4a3d52',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.188.231.133', 9190, 1532345018021),
       (19, '027d26f614afe8b6b3c8efb861c6666985701d76efa70c9d5a02f44c1e0be804ab',
       'ea3396f48653963e3308b355e60bedceb09e1078f0c585a4f5575b2e0e107313',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.221.3.18', 9190, 1532345018021),
       (20, '0225aebdbb8ea2d8c401a638c87da670d5e2f0e4fdb9197f09ae75b2c805046724',
       'ea70a2025987d58c4d4a16ea044071fdddcc291a565f33be8bc3e275cac8e2e2',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.194.71.34', 9190, 1532345018021),
       (21, '02e20add31fbf82b1369e8f2f537df9225a05d6fd497e2f9c65ee9b2df176c01c8',
       '242d0aff07dff674d5808714a223d2d56b2d886691b0e1af28eb3f7586c08d52',
       '0x51c5311F25206De4A9C6ecAa1Bc2Be257B0bA1fb', '35.188.226.52', 9190, 1532345018021);

INSERT INTO delegate2genesis (delegate_id, genesis_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 1),
       (12, 1),
       (13, 1),
       (14, 1),
       (15, 1),
       (16, 1),
       (17, 1),
       (18, 1),
       (19, 1),
       (20, 1),
       (21, 1);