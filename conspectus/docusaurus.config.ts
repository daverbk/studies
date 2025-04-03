import {duotoneDark, oneDark, themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'Conspectus',
  url: 'https://daverbk.github.io',
  baseUrl: '/studies/',
  organizationName: 'daverbk',
  projectName: 'studies',
  favicon: '/img/favicon.ico',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          routeBasePath: '/',
          sidebarPath: './sidebars.ts'
        },
        blog: false,
        theme: {},
      } satisfies Preset.Options,
    ],
  ],

  markdown: {
    mermaid: true
  },
  themes: ['@docusaurus/theme-mermaid'],
  plugins: [
    require.resolve('docusaurus-lunr-search')
  ],

  themeConfig: {

    colorMode: {
      defaultMode: 'dark',
      disableSwitch: false,
      respectPrefersColorScheme: false
    },

    navbar: {
      title: 'Conspectus',
      items: [
        {
          href: 'https://github.com/daverbk/studies',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
